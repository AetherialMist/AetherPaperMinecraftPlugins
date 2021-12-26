package aetherial.aether.essentials.teleportation.command.tp;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.Common;
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
import static aetherial.aether.essentials.teleportation.command.tp.TpRequest.*;

@CommandTag(
    name = TpRegistration.TP_REQUEST_HERE_ALL,
    usage = AetherEssentials.COMMAND_PREFIX + TpRegistration.TP_REQUEST_HERE_ALL,
    desc = "Request to teleport all players to you",
    permission = TpRequestHereAll.PERMISSION
)
public class TpRequestHereAll extends CommandWrapper {

    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TpRegistration.TP_REQUEST_HERE_ALL;

    private final String senderMessagePrefix = applyColor(DEFAULT_COMMAND_COLOR_CODE);
    private final String senderMessageSuffix = applyDefaultMessageColor(" request sent to all players");

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        Optional<Player> optionalPlayer = Common.verifyCommandSenderIsPlayer(commandSender, commandLabel);
        if (optionalPlayer.isEmpty() || !Common.verifyNoArgs(commandSender, args)) {
            return false;
        }
        Player sender = optionalPlayer.get();

        // Send requests
        Collection<? extends Player> players = commandSender.getServer().getOnlinePlayers();
        for (Player accepter : players) {
            // If the player has auto-deny enabled, do not send a request
            if (TpToggle.getInstance().getAutoDenyEnabled(accepter) || accepter.getName().equals(sender.getName())) {
                continue;
            }

            // Notify accepter and track the TP request
            // Player#sendMessage(String) is deprecated, but Player implements CommandSender, which does support sendMessage(String)
            // Player#sendMessage(String) is most likely being removed since the super type already has this method.
            //NOSONAR java:S1874
            accepter.sendMessage(ACCEPTER_MESSAGE_PREFIX + sender.getName() + ACCEPTER_MESSAGE_MIDDLE + commandLabel + ACCEPTER_MESSAGE_SUFFIX);
            TpRequestTracker.instance().trackRequest(sender, accepter, accepter, sender);
        }
        sender.sendMessage(senderMessagePrefix + commandLabel + senderMessageSuffix);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] strings) {
        return Collections.emptyList();
    }

}
