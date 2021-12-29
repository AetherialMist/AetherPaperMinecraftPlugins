package com.github.aetherialmist.aether.essentials.warp.command;

import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.Common;
import com.github.aetherialmist.aether.essentials.warp.WarpRegistrar;
import com.github.aetherialmist.aether.essentials.warp.persistence.WarpStorage;
import com.github.aetherialmist.aether.essentials.wrapper.CommandWrapper;
import com.github.aetherialmist.spigot.plugin.annotation.command.CommandTag;
import com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Delete a warp Location
 */
@CommandTag(
    name = WarpRegistrar.DELETE_WARP,
    usage = AetherEssentials.COMMAND_PREFIX + WarpRegistrar.DELETE_WARP + " <warp>",
    desc = "Delete a warp location",
    permission = DeleteWarp.PERMISSION
)
public class DeleteWarp extends CommandWrapper {

    /**
     * The permission associated with this command
     */
    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + WarpRegistrar.DELETE_WARP;

    private final String warpDeletedPrefix = ChatColorFormatter.applyDefaultMessageColor("Warp deleted: " + ChatColorFormatter.DEFAULT_PLAYER_COLOR_CODE);
    private final String warpDeleteFailedPrefix = ChatColorFormatter.applyDefaultMessageColor("Failed to delete warp: " + ChatColorFormatter.DEFAULT_PLAYER_COLOR_CODE);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        Optional<Player> optionalPlayer = Common.verifyCommandSenderIsPlayer(commandSender, commandLabel);
        if (optionalPlayer.isEmpty() || !Common.verifyExactlyOneArg(commandSender, args)) {
            return false;
        }
        Player player = optionalPlayer.get();
        String warpName = args[0];

        boolean deleted = WarpStorage.getInstance().deleteWarpLocation(warpName).isPresent();

        if (deleted) {
            player.sendMessage(warpDeletedPrefix + warpName);
        } else {
            player.sendMessage(warpDeleteFailedPrefix + warpName);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return args.length == 1 ? WarpStorage.getInstance().onTabComplete(args[0]) : Collections.emptyList();
    }

}
