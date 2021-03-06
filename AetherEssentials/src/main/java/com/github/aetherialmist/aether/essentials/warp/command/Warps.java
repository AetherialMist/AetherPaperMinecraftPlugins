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
 * List all warp Locations
 */
@CommandTag(
    name = WarpRegistrar.WARPS,
    usage = AetherEssentials.COMMAND_PREFIX + WarpRegistrar.WARPS,
    desc = "List all available warp locations",
    permission = Warps.PERMISSION
)
public class Warps extends CommandWrapper {

    /**
     * The permission associated with this command
     */
    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + WarpRegistrar.WARPS;

    private final String messagePrefix = ChatColorFormatter.applyDefaultMessageColor("Warp locations: " + ChatColorFormatter.DEFAULT_PLAYER_COLOR_CODE);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        Optional<Player> optionalSender = Common.verifyCommandSenderIsPlayer(commandSender, commandLabel);
        if (optionalSender.isEmpty() || !Common.verifyNoArgs(commandSender, args)) {
            return false;
        }
        Player sender = optionalSender.get();

        List<String> warpNames = WarpStorage.getInstance().getWarpLocationNames();
        String message = String.join(", ", warpNames);

        sender.sendMessage(messagePrefix + message);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return Collections.emptyList();
    }

}
