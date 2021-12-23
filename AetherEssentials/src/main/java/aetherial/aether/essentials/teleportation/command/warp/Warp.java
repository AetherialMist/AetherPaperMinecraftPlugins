package aetherial.aether.essentials.teleportation.command.warp;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.Common;
import aetherial.aether.essentials.chat.ChatColorFormatter;
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

@CommandTag(
    name = TpRegistration.WARP,
    usage = AetherEssentials.COMMAND_PREFIX + TpRegistration.WARP + " <warp>",
    desc = "Teleport to a warp location",
    permission = Warp.PERMISSION
)
public class Warp extends CommandWrapper implements TabCompleteWrapper {

    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TpRegistration.WARP;

    private final String warpDoesNotExistPrefix;

    public Warp() {
        String textColor = ChatColorFormatter.DEFAULT_MESSAGE_COLOR_CODE;
        this.warpDoesNotExistPrefix = ChatColorFormatter.applyColor(textColor + "Warp does not exist: ");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Optional<Player> optionalSender = Common.verifyCommandSenderIsPlayer(commandSender, label);
        if (optionalSender.isEmpty() || !Common.verifyExactlyOneArg(commandSender, args)) {
            return false;
        }
        Player player = optionalSender.get();
        String warpName = args[0];

        Optional<Location> optional = WarpStorage.getInstance().getWarpLocation(warpName);
        if (optional.isEmpty()) {
            player.sendMessage(warpDoesNotExistPrefix + warpName);
            return true;
        }

        if (player.hasPermission(Back.PERMISSION_ON_TP)) {
            TpHistoryTracker.getInstance().updateBeforeLocation(player, player.getLocation());
        }

        player.teleport(optional.get());

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return args.length == 1 ? WarpStorage.getInstance().onTabComplete(args[0]) : Collections.emptyList();
    }

}
