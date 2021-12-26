package aetherial.aether.essentials.teleportation.command.home;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.Common;
import aetherial.aether.essentials.teleportation.TpRegistration;
import aetherial.aether.essentials.wrapper.CommandWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

import static aetherial.aether.essentials.chat.ChatColorFormatter.DEFAULT_PLAYER_COLOR_CODE;
import static aetherial.aether.essentials.chat.ChatColorFormatter.applyDefaultMessageColor;
import static aetherial.aether.essentials.teleportation.command.home.Home.DEFAULT_HOME_LABEL;

@CommandTag(
    name = TpRegistration.DELETE_HOME,
    usage = "/" + TpRegistration.DELETE_HOME + " [string]",
    desc = "Delete a home",
    permission = DeleteHome.PERMISSION
)
public class DeleteHome extends CommandWrapper {

    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TpRegistration.DELETE_HOME;

    private final String homeDeletedPrefix = applyDefaultMessageColor("Home deleted: " + DEFAULT_PLAYER_COLOR_CODE);
    private final String homeDeleteFailedPrefix = applyDefaultMessageColor("Failed to delete home: " + DEFAULT_PLAYER_COLOR_CODE);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        //noinspection DuplicatedCode No meaningful way to not duplicate
        Optional<Player> optionalPlayer = Common.verifyCommandSenderIsPlayer(commandSender, commandLabel);
        if (optionalPlayer.isEmpty() || !Common.verifyUpToNumberOfArgs(commandSender, args, 1)) {
            return false;
        }
        Player player = optionalPlayer.get();
        String homeLabel = args.length > 0 ? args[0] : DEFAULT_HOME_LABEL;

        boolean deleted = HomeStorage.getInstance().deleteHome(player, homeLabel).isPresent();
        String message = deleted ? homeDeletedPrefix : homeDeleteFailedPrefix;
        commandSender.sendMessage(message + commandLabel);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return HomeStorage.getInstance().verifyIsPlayerOnTabComplete(commandSender, commandLabel, args);
    }

}
