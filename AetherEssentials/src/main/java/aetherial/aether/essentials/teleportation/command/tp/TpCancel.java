package aetherial.aether.essentials.teleportation.command.tp;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.Common;
import aetherial.aether.essentials.teleportation.TpRegistration;
import aetherial.aether.essentials.wrapper.CommandWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static aetherial.aether.essentials.chat.ChatColorFormatter.*;

@CommandTag(
    name = TpRegistration.TP_CANCEL,
    usage = AetherEssentials.COMMAND_PREFIX + TpRegistration.TP_CANCEL,
    desc = "Cancel your active pending teleport request",
    permission = TpCancel.PERMISSION
)
public class TpCancel extends CommandWrapper {

    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TpRegistration.TP_CANCEL;

    private final String noPendingRequests = applyDefaultMessageColor("You don't have any requests to cancel");
    private final String accepterMessagePrefix = applyColor(DEFAULT_PLAYER_COLOR_CODE);
    private final String accepterMessageSuffix = applyDefaultMessageColor(" canceled their tp request");
    private final String senderMessagePrefix = applyDefaultMessageColor("You canceled your request to: " + DEFAULT_PLAYER_COLOR_CODE);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Optional<Player> optionalPlayer = Common.verifyCommandSenderIsPlayer(commandSender, label);
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
