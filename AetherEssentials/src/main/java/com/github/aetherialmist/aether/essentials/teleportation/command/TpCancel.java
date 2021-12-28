package com.github.aetherialmist.aether.essentials.teleportation.command;

import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.Common;
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
    name = TeleportationRegistrar.TP_CANCEL,
    usage = AetherEssentials.COMMAND_PREFIX + TeleportationRegistrar.TP_CANCEL,
    desc = "Cancel your active pending teleport request",
    permission = TpCancel.PERMISSION
)
public class TpCancel extends CommandWrapper {

    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TeleportationRegistrar.TP_CANCEL;

    private final String noPendingRequests = applyDefaultMessageColor("You don't have any requests to cancel");
    private final String accepterMessagePrefix = applyColor(DEFAULT_PLAYER_COLOR_CODE);
    private final String accepterMessageSuffix = applyDefaultMessageColor(" canceled their tp request");
    private final String senderMessagePrefix = applyDefaultMessageColor("You canceled your request to: " + DEFAULT_PLAYER_COLOR_CODE);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        Optional<Player> optionalPlayer = Common.verifyCommandSenderIsPlayer(commandSender, commandLabel);
        if (optionalPlayer.isEmpty() || !Common.verifyNoArgs(commandSender, args)) {
            return false;
        }
        Player sender = optionalPlayer.get();

        // Cancel the request
        Optional<TpRequestRecord> optional = TpRequestTracker.instance().cancelRequest(sender);
        if (optional.isEmpty()) {
            sender.sendMessage(this.noPendingRequests);
            return true;
        }
        TpRequestRecord request = optional.get();
        Player accepter = request.accepter();

        // Notify the Players
        accepter.sendMessage(this.accepterMessagePrefix + sender.getName() + this.accepterMessageSuffix);
        sender.sendMessage(senderMessagePrefix + accepter.getName());

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return Collections.emptyList();
    }

}
