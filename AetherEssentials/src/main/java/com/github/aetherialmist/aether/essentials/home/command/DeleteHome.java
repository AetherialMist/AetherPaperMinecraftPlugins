package com.github.aetherialmist.aether.essentials.home.command;

import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.Common;
import com.github.aetherialmist.aether.essentials.home.HomeRegistrar;
import com.github.aetherialmist.aether.essentials.home.persistence.HomeStorage;
import com.github.aetherialmist.aether.essentials.wrapper.CommandWrapper;
import com.github.aetherialmist.spigot.plugin.annotation.command.CommandTag;
import com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

/**
 * Delete the Player's home
 */
@CommandTag(
    name = HomeRegistrar.DELETE_HOME,
    usage = "/" + HomeRegistrar.DELETE_HOME + " [string]",
    desc = "Delete a home",
    permission = DeleteHome.PERMISSION
)
public class DeleteHome extends CommandWrapper {

    /**
     * The permission associated with this command
     */
    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + HomeRegistrar.DELETE_HOME;

    private final String homeDeletedPrefix = ChatColorFormatter.applyDefaultMessageColor("Home deleted: " + ChatColorFormatter.DEFAULT_PLAYER_COLOR_CODE);
    private final String homeDeleteFailedPrefix = ChatColorFormatter.applyDefaultMessageColor("Failed to delete home: " + ChatColorFormatter.DEFAULT_PLAYER_COLOR_CODE);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        //noinspection DuplicatedCode No meaningful way to not duplicate
        Optional<Player> optionalPlayer = Common.verifyCommandSenderIsPlayer(commandSender, commandLabel);
        if (optionalPlayer.isEmpty() || !Common.verifyUpToNumberOfArgs(commandSender, args, 1)) {
            return false;
        }
        Player player = optionalPlayer.get();
        String homeLabel = args.length > 0 ? args[0] : Home.DEFAULT_HOME_LABEL;

        boolean deleted = HomeStorage.getInstance().deleteHome(player, homeLabel).isPresent();
        String message = deleted ? homeDeletedPrefix : homeDeleteFailedPrefix;
        commandSender.sendMessage(message + homeLabel);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return HomeStorage.getInstance().verifyIsPlayerOnTabComplete(commandSender, commandLabel, args);
    }

}
