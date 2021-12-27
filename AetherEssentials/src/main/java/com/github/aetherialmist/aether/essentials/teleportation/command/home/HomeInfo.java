package com.github.aetherialmist.aether.essentials.teleportation.command.home;

import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.Common;
import com.github.aetherialmist.aether.essentials.teleportation.TpRegistration;
import com.github.aetherialmist.aether.essentials.wrapper.CommandWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

@CommandTag(
    name = TpRegistration.HOME_INFO,
    desc = "Get the location of a home",
    usage = "/" + TpRegistration.HOME_INFO + " [string|nothing]",
    permission = HomeInfo.PERMISSION
)
public class HomeInfo extends CommandWrapper {

    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TpRegistration.HOME_INFO;

    private final String invalidHomePrefix = ChatColorFormatter.applyDefaultMessageColor("Invalid home: ");

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        //noinspection DuplicatedCode No meaningful way to not duplicate
        Optional<Player> optionalPlayer = Common.verifyCommandSenderIsPlayer(commandSender, commandLabel);
        if (optionalPlayer.isEmpty() || !Common.verifyUpToNumberOfArgs(commandSender, args, 1)) {
            return false;
        }
        Player player = optionalPlayer.get();
        String homeLabel = args.length > 0 ? args[0] : Home.DEFAULT_HOME_LABEL;

        Optional<Location> optionalLocation = HomeStorage.getInstance().getHome(player, homeLabel);
        if (optionalLocation.isEmpty()) {
            player.sendMessage(invalidHomePrefix + homeLabel);
            return true;
        }
        Location location = optionalLocation.get();

        String world = location.getWorld().getName();
        String message = String.format("Home: %s, World: %s, X: %d, Y: %d, Z: %d",
            homeLabel, world, (int) location.getX(), (int) location.getY(), (int) location.getZ());
        player.sendMessage(message);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return HomeStorage.getInstance().verifyIsPlayerOnTabComplete(commandSender, commandLabel, args);
    }

}
