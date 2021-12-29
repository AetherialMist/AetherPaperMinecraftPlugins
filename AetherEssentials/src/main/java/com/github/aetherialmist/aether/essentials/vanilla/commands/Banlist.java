package com.github.aetherialmist.aether.essentials.vanilla.commands;

import com.github.aetherialmist.spigot.plugin.annotation.command.CommandTag;
import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.Common;
import com.github.aetherialmist.aether.essentials.vanilla.VanillaRegistrar;
import com.github.aetherialmist.aether.essentials.wrapper.CommandWrapper;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter.DEFAULT_PLAYER_COLOR_CODE;
import static com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter.applyDefaultMessageColor;

@CommandTag(
    name = VanillaRegistrar.BAN_LIST,
    desc = "Get a list of the banned players",
    usage = AetherEssentials.COMMAND_PREFIX + VanillaRegistrar.BAN_LIST,
    permission = Banlist.PERMISSION
)
public class Banlist extends CommandWrapper {

    /**
     * The permission associated with this command
     */
    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + VanillaRegistrar.BAN_LIST;

    private final String bannedPlayersPrefix = applyDefaultMessageColor("Banned Players: " + DEFAULT_PLAYER_COLOR_CODE);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        if (Common.verifyNoArgs(commandSender, args)) {
            return false;
        }
        Set<BanEntry> banEntries = commandSender.getServer().getBanList(BanList.Type.NAME).getBanEntries();

        List<String> bannedNames = new ArrayList<>();
        for (BanEntry entry : banEntries) {
            bannedNames.add(entry.getTarget());
        }
        String message = bannedPlayersPrefix + String.join(", ", bannedNames);
        commandSender.sendMessage(message);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return Collections.emptyList();
    }

}
