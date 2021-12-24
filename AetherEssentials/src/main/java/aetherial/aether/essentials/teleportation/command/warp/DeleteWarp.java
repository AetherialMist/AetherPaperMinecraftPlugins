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

@CommandTag(
    name = TpRegistration.DELETE_WARP,
    usage = AetherEssentials.COMMAND_PREFIX + TpRegistration.DELETE_WARP + " <warp>",
    desc = "Delete a warp location",
    permission = DeleteWarp.PERMISSION
)
public class DeleteWarp extends CommandWrapper {

    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TpRegistration.DELETE_WARP;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Optional<Player> optionalPlayer = Common.verifyCommandSenderIsPlayer(commandSender, label);
        if (optionalPlayer.isEmpty() || !Common.verifyExactlyOneArg(commandSender, args)) {
            return false;
        }

        return WarpStorage.getInstance().deleteWarpLocation(args[0]).isPresent();
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return args.length == 1 ? WarpStorage.getInstance().onTabComplete(args[0]) : Collections.emptyList();
    }

}
