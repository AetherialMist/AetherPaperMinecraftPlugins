package aetherial.aether.essentials.teleportation.command.tp;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.Common;
import aetherial.aether.essentials.teleportation.TpHistoryTracker;
import aetherial.aether.essentials.teleportation.TpRegistration;
import aetherial.aether.essentials.teleportation.command.Back;
import aetherial.aether.essentials.wrapper.CommandWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static aetherial.aether.essentials.chat.ChatColorFormatter.*;

/**
 * {@value #DESCRIPTION}
 * <p>
 * By setting all the message Strings as class variables, we create
 * them once. Instead of each time the command is used.
 */
@CommandTag(
    name = TpRegistration.TP_REQUEST,
    usage = AetherEssentials.COMMAND_PREFIX + TpRegistration.TP_REQUEST + " <player>",
    desc = TpRequest.DESCRIPTION,
    permission = TpRequest.PERMISSION,
    aliases = {"tpa", "aetpr", "aetpa"}
)
public class TpRequest extends CommandWrapper {

    public static final String DESCRIPTION = "Request to teleport to another player";
    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TpRegistration.TP_REQUEST;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return TpRequest.sendRequest(commandSender, commandLabel, args, false);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return args.length == 1 ? Common.getOnlinePlayerNamesStartsWith(args[0]) : Collections.emptyList();
    }

    //==========================================================================

    private static final String NO_PENDING_REQUESTS = applyDefaultMessageColor("You don't have any pending requests");
    private static final String SENDER_MESSAGE_PREFIX = applyDefaultMessageColor("Request sent to: ");
    private static final String ACCEPTER_MESSAGE_PREFIX = applyColor(DEFAULT_COMMAND_COLOR_CODE);
    private static final String ACCEPTER_MESSAGE_MIDDLE = applyDefaultMessageColor(" has sent a " + DEFAULT_COMMAND_COLOR_CODE);
    private static final String ACCEPTER_MESSAGE_SUFFIX = applyColor(String.format("%s request. Type %s/%s %sto accept or %s/%s %sto decline",
        DEFAULT_MESSAGE_COLOR_CODE, DEFAULT_COMMAND_COLOR_CODE, TpRegistration.TP_ACCEPT, DEFAULT_MESSAGE_COLOR_CODE,
        DEFAULT_COMMAND_COLOR_CODE, TpRegistration.TP_DENY, DEFAULT_MESSAGE_COLOR_CODE));

    public static boolean sendRequest(CommandSender commandSender, String commandLabel, String[] args, boolean here) {
        Optional<Player> optionalPlayer = Common.verifyCommandSenderIsPlayer(commandSender, commandLabel);
        if (optionalPlayer.isEmpty() || !Common.verifyExactlyOneArg(commandSender, args)) {
            return false;
        }
        Player sender = optionalPlayer.get();

        // Validate first argument is an online Player
        Optional<Player> optionalTarget = Common.verifyArgIsOnlinePlayer(commandSender, args[0]);
        if (optionalTarget.isEmpty()) {
            return false;
        }
        Player target = optionalTarget.get();

        sender.sendMessage(SENDER_MESSAGE_PREFIX + target.getName());

        // If the accepter has auto-deny enabled, silently fail
        if (TpToggle.getInstance().getAutoDenyEnabled(target)) {
            return true;
        }

        target.sendMessage(ACCEPTER_MESSAGE_PREFIX + sender.getName() + ACCEPTER_MESSAGE_MIDDLE + commandLabel + ACCEPTER_MESSAGE_SUFFIX);

        Player teleportee = here ? target : sender;
        Player destination = here ? sender : target;

        TpRequestTracker.instance().trackRequest(sender, target, teleportee, destination);
        return true;
    }

    public static Optional<Player> replyToRequest(CommandSender commandSender, String commandLabel, String[] args, boolean accept) {
        // The player that send accept/deny
        Optional<Player> optionalAccepter = Common.verifyCommandSenderIsPlayer(commandSender, commandLabel);
        if (optionalAccepter.isEmpty() || !Common.verifyNoArgs(commandSender, args)) {
            return Optional.empty();
        }
        Player accepter = optionalAccepter.get();

        // Get the TP request
        Optional<TpRequestRecord> optionalRequest = TpRequestTracker.instance().getAndRemoveRequest(accepter);

        // Validate there is a request to accept
        if (optionalRequest.isEmpty()) {
            commandSender.sendMessage(NO_PENDING_REQUESTS);
            return optionalAccepter; // Return the command sender
        }
        TpRequestRecord request = optionalRequest.get();

        if (!accept) {
            return Optional.of(request.sender()); // Return the request sender (not this command's sender)
        }

        Player teleportee = request.teleportee();
        Player destination = request.destination();

        // Track the TP history
        if (teleportee.hasPermission(Back.PERMISSION_ON_TP)) {
            TpHistoryTracker.getInstance().updateBeforeLocation(teleportee, teleportee.getLocation());
        }

        // Teleport the Player
        teleportee.teleport(destination);

        return Optional.of(request.sender()); // Return the request sender (not this command's sender)
    }

}
