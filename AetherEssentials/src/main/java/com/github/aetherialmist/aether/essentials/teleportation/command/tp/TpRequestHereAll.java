package com.github.aetherialmist.aether.essentials.teleportation.command.tp;

import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.Common;
import com.github.aetherialmist.aether.essentials.teleportation.TpRegistration;
import com.github.aetherialmist.aether.essentials.wrapper.CommandWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter.*;

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
            accepter.sendMessage(TpRequest.ACCEPTER_MESSAGE_PREFIX + sender.getName() + TpRequest.ACCEPTER_MESSAGE_MIDDLE + commandLabel + TpRequest.ACCEPTER_MESSAGE_SUFFIX);
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
