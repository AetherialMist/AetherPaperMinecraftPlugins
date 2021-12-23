package aetherial.aether.essentials.teleportation.command.home;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.wrapper.CommandWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

@CommandTag(
    name = "home",
    usage = "/<command> [string|null]",
    desc = "Teleport to your home",
    permission = Home.PERMISSION
)
public class Home extends CommandWrapper {

    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + "home";

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
