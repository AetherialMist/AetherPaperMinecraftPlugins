package com.github.aetherialmist.aether.essentials.vanilla.commands;

import aetherial.spigot.plugin.annotation.command.CommandTag;
import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.Common;
import com.github.aetherialmist.aether.essentials.vanilla.VanillaRegistrar;
import com.github.aetherialmist.aether.essentials.wrapper.CommandWrapper;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;

import java.util.*;

import static com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter.DEFAULT_PLAYER_COLOR_CODE;
import static com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter.applyDefaultMessageColor;

/**
 * Ban a Player that has been on the server and is cached.
 * <p>
 * Because Player's can change their username, bans must be issued to their UUID
 * which can only be known (to this plugin) after they have logged in. If the
 * UUID is known beforehand, their information will have to be manually added
 * to the banned-players.json file of the server.
 */
@CommandTag(
    name = VanillaRegistrar.BAN,
    desc = "Ban a player",
    usage = AetherEssentials.COMMAND_PREFIX + VanillaRegistrar.BAN + " <player>",
    permission = Ban.PERMISSION
)
public class Ban extends CommandWrapper {

    /**
     * The permission associated with this command
     */
    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + VanillaRegistrar.BAN;

    private final String successfulBanPrefix = applyDefaultMessageColor("You have successfully banned: " + DEFAULT_PLAYER_COLOR_CODE);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        if (!Common.verifyMinimumArgs(commandSender, args, 1)) {
            return false;
        }
        String toBanName = args[0];
        Optional<ServerOperator> optionalPlayer = Common.verifyArgIsPlayer(commandSender, toBanName);
        if (optionalPlayer.isEmpty()) {
            return true;
        }
        ServerOperator serverOperatorToBan = optionalPlayer.get();
        List<String> allArgs = new ArrayList<>(Arrays.asList(args));
        allArgs.remove(0); // Remove first arg
        String reason = String.join(" ", allArgs);

        if (serverOperatorToBan instanceof Player player) {
            player.banPlayer(reason, commandSender.getName());
        } else if (serverOperatorToBan instanceof OfflinePlayer offlinePlayer) {
            offlinePlayer.banPlayer(reason, commandSender.getName());
        }

        commandSender.sendMessage(successfulBanPrefix + toBanName);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return args.length == 1 ? Common.getAllPlayerNames() : Collections.emptyList();
    }

}
