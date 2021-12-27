package com.github.aetherialmist.aether.essentials.teleportation.command.tp;

import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.teleportation.TpRegistration;
import com.github.aetherialmist.aether.essentials.wrapper.CommandWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

@CommandTag(
    name = TpRegistration.TP_ACCEPT,
    usage = AetherEssentials.COMMAND_PREFIX + TpRegistration.TP_ACCEPT,
    desc = "Accept the most recent pending teleport request",
    permission = TpAccept.PERMISSION
)
public class TpAccept extends CommandWrapper {

    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TpRegistration.TP_ACCEPT;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        TpRequest.replyToRequest(commandSender, commandLabel, args, true);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return Collections.emptyList();
    }

}
