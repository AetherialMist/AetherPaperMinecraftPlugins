package com.github.aetherialmist.aether.essentials.teleportation.command.warp;

import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.Common;
import com.github.aetherialmist.aether.essentials.teleportation.TpHistoryTracker;
import com.github.aetherialmist.aether.essentials.teleportation.TpRegistration;
import com.github.aetherialmist.aether.essentials.teleportation.command.Back;
import com.github.aetherialmist.aether.essentials.wrapper.CommandWrapper;
import com.github.aetherialmist.aether.essentials.wrapper.TabCompleteWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CommandTag(
    name = TpRegistration.WARP,
    usage = AetherEssentials.COMMAND_PREFIX + TpRegistration.WARP + " <warp>",
    desc = "Teleport to a warp location",
    permission = Warp.PERMISSION
)
public class Warp extends CommandWrapper implements TabCompleteWrapper {

    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TpRegistration.WARP;

    private final String warpDoesNotExistPrefix = ChatColorFormatter.applyDefaultMessageColor("Warp does not exist: " + ChatColorFormatter.DEFAULT_PLAYER_COLOR_CODE);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        //noinspection DuplicatedCode Can't meaninfully not duplicate this code
        Optional<Player> optionalSender = Common.verifyCommandSenderIsPlayer(commandSender, commandLabel);
        if (optionalSender.isEmpty() || !Common.verifyExactlyOneArg(commandSender, args)) {
            return false;
        }
        Player player = optionalSender.get();
        String warpName = args[0];

        Optional<Location> optionalLocation = WarpStorage.getInstance().getWarpLocation(warpName);
        if (optionalLocation.isEmpty()) {
            player.sendMessage(warpDoesNotExistPrefix + warpName);
            return true;
        }

        // Track TP history
        if (player.hasPermission(Back.PERMISSION_ON_TP)) {
            TpHistoryTracker.getInstance().updateBeforeLocation(player, player.getLocation());
        }

        player.teleport(optionalLocation.get());

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        Optional<Player> optionalPlayer = Common.verifyCommandSenderIsPlayer(commandSender, commandLabel);
        if (optionalPlayer.isEmpty()){
            return Collections.emptyList();
        }

        return args.length == 1 ? WarpStorage.getInstance().onTabComplete(args[0]) : Collections.emptyList();
    }

}
