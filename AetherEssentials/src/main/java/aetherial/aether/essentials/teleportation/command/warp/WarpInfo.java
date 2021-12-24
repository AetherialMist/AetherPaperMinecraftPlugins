package aetherial.aether.essentials.teleportation.command.warp;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.Common;
import aetherial.aether.essentials.teleportation.TpRegistration;
import aetherial.aether.essentials.wrapper.CommandWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static aetherial.aether.essentials.chat.ChatColorFormatter.DEFAULT_PLAYER_COLOR_CODE;
import static aetherial.aether.essentials.chat.ChatColorFormatter.applyDefaultMessageColor;

@CommandTag(
    name = TpRegistration.WARP_INFO,
    usage = AetherEssentials.COMMAND_PREFIX + TpRegistration.WARP_INFO + " <warp>",
    desc = "Get the location of a warp",
    permission = WarpInfo.PERMISSION
)
public class WarpInfo extends CommandWrapper {

    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TpRegistration.WARP_INFO;

    private final String warpNotFound = applyDefaultMessageColor("Warp not found: " + DEFAULT_PLAYER_COLOR_CODE);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        //noinspection DuplicatedCode Can't meaninfully not duplicate this code
        Optional<Player> optionalSender = Common.verifyCommandSenderIsPlayer(commandSender, label);
        if (optionalSender.isEmpty() || !Common.verifyExactlyOneArg(commandSender, args)) {
            return false;
        }
        Player sender = optionalSender.get();
        String warpName = args[0];

        Optional<Location> optionalLocation = WarpStorage.getInstance().getWarpLocation(warpName);
        if (optionalLocation.isEmpty()) {
            sender.sendMessage(warpNotFound + warpName);
            return true;
        }
        Location location = optionalLocation.get();

        String world = location.getWorld().getName();
        String message = String.format("Warp: %s, World: %s, X: %d, Y: %d, Z: %d",
            warpName, world, (int) location.getX(), (int) location.getY(), (int) location.getZ());
        sender.sendMessage(message);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return args.length == 1 ? WarpStorage.getInstance().onTabComplete(args[0]) : Collections.emptyList();
    }

}
