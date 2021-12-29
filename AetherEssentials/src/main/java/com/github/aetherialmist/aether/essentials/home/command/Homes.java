package com.github.aetherialmist.aether.essentials.home.command;

import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.Common;
import com.github.aetherialmist.aether.essentials.home.HomeRegistrar;
import com.github.aetherialmist.aether.essentials.home.persistence.HomeStorage;
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
 * List the Player's homes
 */
@CommandTag(
    name = HomeRegistrar.HOMES,
    desc = "List all your homes",
    usage = "/" + HomeRegistrar.HOMES,
    permission = Homes.PERMISSION
)
public class Homes extends CommandWrapper {

    /**
     * The permission associated with this command
     */
    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + HomeRegistrar.HOMES;

    private final String messagePrefix = ChatColorFormatter.applyDefaultMessageColor("Homes: ");

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        Optional<Player> optionalPlayer = Common.verifyCommandSenderIsPlayer(commandSender, commandLabel);
        if (optionalPlayer.isEmpty() || !Common.verifyNoArgs(commandSender, args)) {
            return false;
        }
        Player player = optionalPlayer.get();

        List<String> homeNames = HomeStorage.getInstance().getHomeNames(player);
        String messageSuffix = String.join(", ", homeNames);
        player.sendMessage(messagePrefix + messageSuffix);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return Collections.emptyList();
    }

}
