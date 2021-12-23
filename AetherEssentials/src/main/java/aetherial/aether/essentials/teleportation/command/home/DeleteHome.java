package aetherial.aether.essentials.teleportation.command.home;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.wrapper.CommandWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

@CommandTag(
    name = "delhome",
    usage = "/<command> [string]",
    desc = "Delete a home",
    permission = DeleteHome.PERMISSION
)
public class DeleteHome extends CommandWrapper {

    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + "delhome";

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        // TODO
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] strings) {
        return Collections.emptyList();
    }

}
