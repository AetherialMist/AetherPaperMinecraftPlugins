package aetherial.aether.essentials.teleportation.command.tp;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.teleportation.TpRegistration;
import aetherial.aether.essentials.wrapper.CommandWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * {@value #DESCRIPTION}
 */
@CommandTag(
    name = TpRegistration.TP_ACCEPT,
    usage = AetherEssentials.COMMAND_PREFIX + TpRegistration.TP_ACCEPT,
    desc = TpAccept.DESCRIPTION,
    permission = TpAccept.PERMISSION
)
public class TpAccept extends CommandWrapper {

    public static final String DESCRIPTION = "Accept the most recent pending teleport request";
    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TpRegistration.TP_ACCEPT;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        return TpRequest.replyToRequest(commandSender, label, args, true).isPresent();
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return Collections.emptyList();
    }

}
