package com.github.aetherialmist.aether.essentials.vanilla.commands;

import com.github.aetherialmist.spigot.plugin.annotation.command.CommandTag;
import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.Common;
import com.github.aetherialmist.aether.essentials.vanilla.VanillaRegistrar;
import com.github.aetherialmist.aether.essentials.wrapper.CommandWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter.DEFAULT_PLAYER_COLOR_CODE;
import static com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter.applyDefaultMessageColor;

/**
 * IP Ban an online Player
 */
@CommandTag(
    name = VanillaRegistrar.BAN_IP,
    desc = "IP ban an online player",
    usage = AetherEssentials.COMMAND_PREFIX + VanillaRegistrar.BAN_IP + " <player>",
    permission = BanIp.PERMISSION
)
public class BanIp extends CommandWrapper {

    /**
     * The permission associated with this command
     */
    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + VanillaRegistrar.BAN_IP;

    private final String successfulIpBanPrefix = applyDefaultMessageColor("You have successfully IP banned: " + DEFAULT_PLAYER_COLOR_CODE);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        if (!Common.verifyMinimumArgs(commandSender, args, 1)) {
            return false;
        }
        String toBanName = args[0];
        Optional<Player> optionalServerOperator = Common.verifyArgIsOnlinePlayer(commandSender, toBanName);
        if (optionalServerOperator.isEmpty()) {
            return false;
        }
        Player playerToBanIp = optionalServerOperator.get();
        List<String> allArgs = new ArrayList<>(Arrays.asList(args));
        allArgs.remove(0); // Remove first arg
        String reason = String.join(" ", allArgs);

        playerToBanIp.banPlayerIP(reason, commandSender.getName());

        commandSender.sendMessage(successfulIpBanPrefix + toBanName);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return Common.getOnlinePlayerNames();
    }

}
