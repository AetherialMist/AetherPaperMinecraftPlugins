package com.github.aetherialmist.aether.essentials.teleportation.command;

import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.Common;
import com.github.aetherialmist.aether.essentials.teleportation.persistence.TpHistoryTracker;
import com.github.aetherialmist.aether.essentials.teleportation.TeleportationRegistrar;
import com.github.aetherialmist.aether.essentials.teleportation.persistence.TpRequestRecord;
import com.github.aetherialmist.aether.essentials.teleportation.persistence.TpRequestTracker;
import com.github.aetherialmist.aether.essentials.wrapper.CommandWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter.*;

@CommandTag(
    name = TeleportationRegistrar.TP_REQUEST,
    usage = AetherEssentials.COMMAND_PREFIX + TeleportationRegistrar.TP_REQUEST + " <player>",
    desc = "Request to teleport to another player",
    permission = TpRequest.PERMISSION
)
public class TpRequest extends CommandWrapper {

    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TeleportationRegistrar.TP_REQUEST;

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
    private static final String SENDER_MESSAGE_PREFIX = applyDefaultMessageColor("You sent a " + DEFAULT_COMMAND_COLOR_CODE);
    private static final String SENDER_MESSAGE_MIDDLE = applyDefaultMessageColor(" request sent to: " + DEFAULT_PLAYER_COLOR_CODE);
    public static final String ACCEPTER_MESSAGE_PREFIX = applyColor(DEFAULT_PLAYER_COLOR_CODE);
    public static final String ACCEPTER_MESSAGE_MIDDLE = applyDefaultMessageColor(" has sent a " + DEFAULT_COMMAND_COLOR_CODE);
    public static final String ACCEPTER_MESSAGE_SUFFIX = applyColor(String.format("%s request. Type %s/%s %sto accept or %s/%s %sto decline",
        DEFAULT_MESSAGE_COLOR_CODE, DEFAULT_COMMAND_COLOR_CODE, TeleportationRegistrar.TP_ACCEPT, DEFAULT_MESSAGE_COLOR_CODE,
        DEFAULT_COMMAND_COLOR_CODE, TeleportationRegistrar.TP_DENY, DEFAULT_MESSAGE_COLOR_CODE));

    public static boolean sendRequest(CommandSender commandSender, String commandLabel, String[] args, boolean here) {
        Optional<Player> optionalPlayer = Common.verifyCommandSenderIsPlayer(commandSender, commandLabel);
        if (optionalPlayer.isEmpty() || !Common.verifyExactlyOneArg(commandSender, args)) {
            return false;
        }
        Player sender = optionalPlayer.get();

        // Validate first argument is an online Player
        Optional<Player> optionalAccepter = Common.verifyArgIsOnlinePlayer(commandSender, args[0]);
        if (optionalAccepter.isEmpty()) {
            return false;
        }
        Player accepter = optionalAccepter.get();

        sender.sendMessage(SENDER_MESSAGE_PREFIX + commandLabel + SENDER_MESSAGE_MIDDLE + accepter.getName());

        // If the accepter has auto-deny enabled, silently fail
        if (TpToggle.getInstance().getAutoDenyEnabled(accepter)) {
            return true;
        }

        accepter.sendMessage(ACCEPTER_MESSAGE_PREFIX + sender.getName() + ACCEPTER_MESSAGE_MIDDLE + commandLabel + ACCEPTER_MESSAGE_SUFFIX);

        Player teleportee = here ? accepter : sender;
        Player destination = here ? sender : accepter;

        TpRequestTracker.instance().trackRequest(sender, accepter, teleportee, destination);
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
            return Optional.empty(); // Return the command sender
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
