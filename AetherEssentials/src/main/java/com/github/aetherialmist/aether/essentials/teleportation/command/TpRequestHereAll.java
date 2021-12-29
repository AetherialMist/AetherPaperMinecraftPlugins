package com.github.aetherialmist.aether.essentials.teleportation.command;

import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.Common;
import com.github.aetherialmist.aether.essentials.teleportation.TeleportationRegistrar;
import com.github.aetherialmist.aether.essentials.teleportation.persistence.TpRequestTracker;
import com.github.aetherialmist.aether.essentials.wrapper.CommandWrapper;
import com.github.aetherialmist.spigot.plugin.annotation.command.CommandTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter.*;

/**
 * Request all online players to teleport to the sender
 */
@CommandTag(
    name = TeleportationRegistrar.TP_REQUEST_HERE_ALL,
    usage = AetherEssentials.COMMAND_PREFIX + TeleportationRegistrar.TP_REQUEST_HERE_ALL,
    desc = "Request to teleport all players to you",
    permission = TpRequestHereAll.PERMISSION
)
public class TpRequestHereAll extends CommandWrapper {

    /**
     * The permission associated with this command
     */
    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TeleportationRegistrar.TP_REQUEST_HERE_ALL;

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
