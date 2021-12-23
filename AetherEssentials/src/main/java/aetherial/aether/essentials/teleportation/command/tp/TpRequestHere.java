package aetherial.aether.essentials.teleportation.command.tp;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.Common;
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
    name = TpRegistration.TP_REQUEST_HERE,
    usage = AetherEssentials.COMMAND_PREFIX + TpRegistration.TP_REQUEST + "<player>",
    desc = TpRequestHere.DESCRIPTION,
    permission = TpRequestHere.PERMISSION,
    aliases = {"tpahere", "aetprhere", "aetpahere"}
)
public class TpRequestHere extends CommandWrapper {

    public static final String DESCRIPTION = "Request another player to teleport to you";
    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TpRegistration.TP_REQUEST_HERE;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        return TpRequest.sendRequest(commandSender, label, args, true);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return args.length == 1 ? Common.getOnlinePlayerNamesStartsWith(args[0]) : Collections.emptyList();
    }

}
