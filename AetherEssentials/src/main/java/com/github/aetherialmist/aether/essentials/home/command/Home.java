package com.github.aetherialmist.aether.essentials.home.command;

import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.Common;
import com.github.aetherialmist.aether.essentials.home.HomeRegistrar;
import com.github.aetherialmist.aether.essentials.home.persistence.HomeStorage;
import com.github.aetherialmist.aether.essentials.teleportation.persistence.TpHistoryTracker;
import com.github.aetherialmist.aether.essentials.teleportation.command.Back;
import com.github.aetherialmist.aether.essentials.wrapper.CommandWrapper;
import com.github.aetherialmist.spigot.plugin.annotation.command.CommandTag;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

import static com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter.DEFAULT_PLAYER_COLOR_CODE;
import static com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter.applyDefaultMessageColor;

/**
 * Teleport to the Player's home
 */
@CommandTag(
    name = HomeRegistrar.HOME,
    usage = "/" + HomeRegistrar.HOME + " <home>",
    desc = "Teleport to your home",
    permission = Home.PERMISSION
)
public class Home extends CommandWrapper {

    /**
     * The permission associated with this command
     */
    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + HomeRegistrar.HOME;

    /**
     * The home label to use if no argument is passed
     */
    public static final String DEFAULT_HOME_LABEL = "home";

    private final String notValidHomePrefix = applyDefaultMessageColor("Not a valid home: " + DEFAULT_PLAYER_COLOR_CODE);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        //noinspection DuplicatedCode No meaningful way to not duplicate
        Optional<Player> optionalPlayer = Common.verifyCommandSenderIsPlayer(commandSender, commandLabel);
        if (optionalPlayer.isEmpty() || !Common.verifyUpToNumberOfArgs(commandSender, args, 1)) {
            return false;
        }
        Player player = optionalPlayer.get();
        String homeLabel = args.length > 0 ? args[0] : DEFAULT_HOME_LABEL;

        Optional<Location> optionalLocation = HomeStorage.getInstance().getHome(player, homeLabel);
        if (optionalLocation.isEmpty()) {
            commandSender.sendMessage(notValidHomePrefix + homeLabel);
            return true;
        }

        if (player.hasPermission(Back.PERMISSION_ON_TP)) {
            TpHistoryTracker.getInstance().updateBeforeLocation(player, player.getLocation());
        }

        player.teleport(optionalLocation.get());

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return HomeStorage.getInstance().verifyIsPlayerOnTabComplete(commandSender, commandLabel, args);
    }

}
