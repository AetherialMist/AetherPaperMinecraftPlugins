package com.github.aetherialmist.aether.essentials.warp.command;

import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.Common;
import com.github.aetherialmist.aether.essentials.warp.WarpRegistrar;
import com.github.aetherialmist.aether.essentials.warp.persistence.WarpStorage;
import com.github.aetherialmist.aether.essentials.wrapper.CommandWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Set a warp Location
 */
@CommandTag(
    name = WarpRegistrar.SET_WARP,
    usage = AetherEssentials.COMMAND_PREFIX + WarpRegistrar.SET_WARP + " <warp>",
    desc = "Set a warp location",
    permission = SetWarp.PERMISSION
)
public class SetWarp extends CommandWrapper {

    /**
     * The permission associated with this command
     */
    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + WarpRegistrar.SET_WARP;

    private final String warpCreatedPrefix = ChatColorFormatter.applyDefaultMessageColor("Warp created: " + ChatColorFormatter.DEFAULT_PLAYER_COLOR_CODE);
    private final String warpCreateFailedPrefix = ChatColorFormatter.applyDefaultMessageColor("Failed to create warp: " + ChatColorFormatter.DEFAULT_PLAYER_COLOR_CODE);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        Optional<Player> optionalPlayer = Common.verifyCommandSenderIsPlayer(commandSender, commandLabel);
        if (optionalPlayer.isEmpty() || !Common.verifyExactlyOneArg(commandSender, args)) {
            return false;
        }
        Player player = optionalPlayer.get();
        String warpName = args[0];

        boolean created = WarpStorage.getInstance().setWarpLocation(warpName, player.getLocation()).isPresent();
        String messageBase = created ? warpCreatedPrefix : warpCreateFailedPrefix;
        player.sendMessage(messageBase + warpName);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] strings) {
        return Collections.emptyList();
    }

}
