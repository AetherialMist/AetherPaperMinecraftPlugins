package com.github.aetherialmist.aether.essentials.home.command;

import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.Common;
import com.github.aetherialmist.aether.essentials.home.HomeRegistrar;
import com.github.aetherialmist.aether.essentials.home.persistence.HomeStorage;
import com.github.aetherialmist.aether.essentials.wrapper.CommandWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter.DEFAULT_PLAYER_COLOR_CODE;
import static com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter.applyDefaultMessageColor;
import static com.github.aetherialmist.aether.essentials.home.command.Home.DEFAULT_HOME_LABEL;

/**
 * The set-home's command logic
 */
@CommandTag(
    name = HomeRegistrar.SET_HOME,
    usage = "/" + HomeRegistrar.SET_HOME + " <home>",
    desc = "Set your home",
    permission = SetHome.PERMISSION
)
public class SetHome extends CommandWrapper {

    /**
     * The permission associated with this command
     */
    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + HomeRegistrar.SET_HOME;

    private final String homeCreatedPrefix = applyDefaultMessageColor("Home set: " + DEFAULT_PLAYER_COLOR_CODE);
    private final String homeCreateFailedPrefix = applyDefaultMessageColor("Failed to set home: " + DEFAULT_PLAYER_COLOR_CODE);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        //noinspection DuplicatedCode No meaningful way to not duplicate
        Optional<Player> optionalPlayer = Common.verifyCommandSenderIsPlayer(commandSender, commandLabel);
        if (optionalPlayer.isEmpty() || !Common.verifyUpToNumberOfArgs(commandSender, args, 1)) {
            return false;
        }
        Player player = optionalPlayer.get();
        String homeLabel = args.length > 0 ? args[0] : DEFAULT_HOME_LABEL;

        boolean created = HomeStorage.getInstance().setHome(player, homeLabel).isPresent();
        String message = created ? homeCreatedPrefix : homeCreateFailedPrefix;
        commandSender.sendMessage(message + homeLabel);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] strings) {
        return Collections.emptyList();
    }

}
