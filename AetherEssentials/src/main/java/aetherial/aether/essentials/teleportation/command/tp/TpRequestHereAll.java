package aetherial.aether.essentials.teleportation.command.tp;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.Common;
import aetherial.aether.essentials.chat.ChatColorFormatter;
import aetherial.aether.essentials.teleportation.TpRegistration;
import aetherial.aether.essentials.wrapper.CommandWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
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
    name = TpRegistration.TP_REQUEST_HERE_ALL,
    usage = AetherEssentials.COMMAND_PREFIX + TpRegistration.TP_REQUEST_HERE_ALL,
    desc = TpRequestHereAll.DESCRIPTION,
    permission = TpRequestHereAll.PERMISSION
)
public class TpRequestHereAll extends CommandWrapper {

    public static final String DESCRIPTION = "Request to teleport all players to you";
    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TpRegistration.TP_REQUEST_HERE_ALL;

    private final String senderMessage = applyDefaultMessageColor("Request sent to all players");
    private final String accepterMessagePrefix = applyColor(DEFAULT_COMMAND_COLOR_CODE);
    private final String accepterMessageSuffix = applyColor(String.format("%s has sent a %s%s%s request. Type %s/%s %sto accept or %s/%s %sto decline",
        DEFAULT_MESSAGE_COLOR_CODE, DEFAULT_COMMAND_COLOR_CODE, TpRegistration.TP_REQUEST_HERE_ALL, DEFAULT_MESSAGE_COLOR_CODE,
        DEFAULT_COMMAND_COLOR_CODE, TpRegistration.TP_ACCEPT, DEFAULT_MESSAGE_COLOR_CODE, DEFAULT_COMMAND_COLOR_CODE,
        TpRegistration.TP_DENY, DEFAULT_MESSAGE_COLOR_CODE));

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Optional<Player> optionalPlayer = Common.verifyCommandSenderIsPlayer(commandSender, label);
        if (optionalPlayer.isEmpty() || !Common.verifyNoArgs(commandSender, args)) {
            return false;
        }
        Player sender = optionalPlayer.get();

        // Send requests
        Collection<? extends Player> players = commandSender.getServer().getOnlinePlayers();
        for (Player target : players) {
            // If the player has auto-deny enabled, do not send a request
            if (TpToggle.getInstance().getAutoDenyEnabled(target) || target.getName().equals(sender.getName())) {
                continue;
            }

            // Notify accepter and track the TP request
            target.sendMessage(accepterMessagePrefix + sender.getName() + accepterMessageSuffix);
            TpRequestTracker.instance().trackRequest(sender, target, target, sender);
        }
        sender.sendMessage(senderMessage);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] strings) {
        return Collections.emptyList();
    }

}
