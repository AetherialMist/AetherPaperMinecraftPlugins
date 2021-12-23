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

import static aetherial.aether.essentials.chat.ChatColorFormatter.DEFAULT_COMMAND_COLOR_CODE;
import static aetherial.aether.essentials.chat.ChatColorFormatter.applyDefaultMessageColor;

/**
 * {@value #DESCRIPTION}
 * <p>
 * By setting all the message Strings as class variables, we create
 * them once. Instead of each time the command is used.
 */
@CommandTag(
    name = TpRegistration.TP_DENY,
    usage = AetherEssentials.COMMAND_PREFIX + TpRegistration.TP_DENY,
    desc = TpDeny.DESCRIPTION,
    permission = TpDeny.PERMISSION
)
public class TpDeny extends CommandWrapper {

    public static final String DESCRIPTION = "Decline the most recent pending teleport request";
    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TpRegistration.TP_DENY;

    private final String senderMessage = applyDefaultMessageColor("Your teleport request was denied");
    private final String accepterMessagePrefix = applyDefaultMessageColor("You denied the teleport request from: " + DEFAULT_COMMAND_COLOR_CODE);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        // Get the sender of the request (not this command's sender)
        Optional<Player> optionalRequestSender = TpRequest.replyToRequest(commandSender, label, args, false);
        if (optionalRequestSender.isEmpty()) {
            return false;
        }
        Player requestSender = optionalRequestSender.get();

        commandSender.sendMessage(accepterMessagePrefix + requestSender.getName());
        requestSender.sendMessage(senderMessage);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return Collections.emptyList();
    }

}
