package aetherial.aether.essentials.teleportation.command.warp;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.Common;
import aetherial.aether.essentials.teleportation.TpHistoryTracker;
import aetherial.aether.essentials.teleportation.TpRegistration;
import aetherial.aether.essentials.teleportation.command.Back;
import aetherial.aether.essentials.wrapper.CommandWrapper;
import aetherial.aether.essentials.wrapper.TabCompleteWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static aetherial.aether.essentials.chat.ChatColorFormatter.applyDefaultMessageColor;

@CommandTag(
    name = TpRegistration.WARP,
    usage = AetherEssentials.COMMAND_PREFIX + TpRegistration.WARP + " <warp>",
    desc = "Teleport to a warp location",
    permission = Warp.PERMISSION
)
public class Warp extends CommandWrapper implements TabCompleteWrapper {

    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TpRegistration.WARP;

    private final String warpDoesNotExistPrefix = applyDefaultMessageColor("Warp does not exist: ");

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        //noinspection DuplicatedCode Can't meaninfully not duplicate this code
        Optional<Player> optionalSender = Common.verifyCommandSenderIsPlayer(commandSender, label);
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
        return args.length == 1 ? WarpStorage.getInstance().onTabComplete(args[0]) : Collections.emptyList();
    }

}
