package com.github.aetherialmist.aether.essentials.warp.command;

import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.Common;
import com.github.aetherialmist.aether.essentials.warp.WarpRegistrar;
import com.github.aetherialmist.aether.essentials.warp.persistence.WarpStorage;
import com.github.aetherialmist.aether.essentials.wrapper.CommandWrapper;
import com.github.aetherialmist.spigot.plugin.annotation.command.CommandTag;
import com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Get information about a warp
 */
@CommandTag(
    name = WarpRegistrar.WARP_INFO,
    usage = AetherEssentials.COMMAND_PREFIX + WarpRegistrar.WARP_INFO + " <warp>",
    desc = "Get the location of a warp",
    permission = WarpInfo.PERMISSION
)
public class WarpInfo extends CommandWrapper {

    /**
     * The permission associated with this command
     */
    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + WarpRegistrar.WARP_INFO;

    private final String warpNotFound = ChatColorFormatter.applyDefaultMessageColor("Warp not found: " + ChatColorFormatter.DEFAULT_PLAYER_COLOR_CODE);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        //noinspection DuplicatedCode Can't meaninfully not duplicate this code
        Optional<Player> optionalSender = Common.verifyCommandSenderIsPlayer(commandSender, commandLabel);
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
