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
    name = TpRegistration.SET_WARP,
    usage = AetherEssentials.COMMAND_PREFIX + TpRegistration.SET_WARP + " <warp>",
    desc = "Set a warp location",
    permission = SetWarp.PERMISSION
)
public class SetWarp extends CommandWrapper {

    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TpRegistration.SET_WARP;

    private final String warpCreatedPrefix = applyDefaultMessageColor("Warp created: " + DEFAULT_PLAYER_COLOR_CODE);
    private final String warpCreateFailedPrefix = applyDefaultMessageColor("Failed to create warp: " + DEFAULT_PLAYER_COLOR_CODE);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Optional<Player> optionalPlayer = Common.verifyCommandSenderIsPlayer(commandSender, label);
        if (optionalPlayer.isEmpty() || !Common.verifyExactlyOneArg(commandSender, args)) {
            return false;
        }
        Player player = optionalPlayer.get();
        String warpName = args[0];

        boolean created = WarpStorage.getInstance().setWarpLocation(warpName, player.getLocation()).isPresent();

        if (created) {
            player.sendMessage(warpCreatedPrefix + warpName);
        } else {
            player.sendMessage(warpCreateFailedPrefix + warpName);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] strings) {
        return Collections.emptyList();
    }

}
