package aetherial.aether.essentials.teleportation.command;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.wrapper.CommandWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

// TODO: Remove
@CommandTag(
    name = "testing",
    desc = "Test command",
    permission = Testing.PERMISSION,
    usage = "/<command>"
)
public class Testing extends CommandWrapper {

    private final Logger log = LogManager.getLogger(AetherEssentials.PLUGIN_NAME);

    public static final String COMMAND_LABEL = "testing";
    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + COMMAND_LABEL;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        //noinspection DuplicatedCode
        if (!(commandSender instanceof Player player)) {
            return false;
        }

        String location = player.getLocation().toString();
        String world = player.getWorld().toString();
        log.info(location);
        log.info(world);
        log.info(player.getWorld().getUID());

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] strings) {
        return Collections.emptyList();
    }

}
