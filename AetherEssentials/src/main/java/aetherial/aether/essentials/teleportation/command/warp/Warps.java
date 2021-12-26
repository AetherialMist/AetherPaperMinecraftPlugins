package aetherial.aether.essentials.teleportation.command.warp;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.Common;
import aetherial.aether.essentials.teleportation.TpRegistration;
import aetherial.aether.essentials.wrapper.CommandWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static aetherial.aether.essentials.chat.ChatColorFormatter.DEFAULT_PLAYER_COLOR_CODE;
import static aetherial.aether.essentials.chat.ChatColorFormatter.applyDefaultMessageColor;

@CommandTag(
    name = TpRegistration.WARPS,
    usage = AetherEssentials.COMMAND_PREFIX + TpRegistration.WARPS,
    desc = "List all available warp locations",
    permission = Warps.PERMISSION
)
public class Warps extends CommandWrapper {

    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TpRegistration.WARPS;

    private final String messagePrefix = applyDefaultMessageColor("Warp locations: " + DEFAULT_PLAYER_COLOR_CODE);

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
