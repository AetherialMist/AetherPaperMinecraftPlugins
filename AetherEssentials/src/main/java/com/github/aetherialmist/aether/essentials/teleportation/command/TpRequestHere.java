package com.github.aetherialmist.aether.essentials.teleportation.command;

import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.Common;
import com.github.aetherialmist.aether.essentials.teleportation.TeleportationRegistrar;
import com.github.aetherialmist.aether.essentials.wrapper.CommandWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

@CommandTag(
    name = TeleportationRegistrar.TP_REQUEST_HERE,
    usage = AetherEssentials.COMMAND_PREFIX + TeleportationRegistrar.TP_REQUEST + "<player>",
    desc = "Request another player to teleport to you",
    permission = TpRequestHere.PERMISSION
)
public class TpRequestHere extends CommandWrapper {

    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TeleportationRegistrar.TP_REQUEST_HERE;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return TpRequest.sendRequest(commandSender, commandLabel, args, true);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return args.length == 1 ? Common.getOnlinePlayerNamesStartsWith(args[0]) : Collections.emptyList();
    }

}
