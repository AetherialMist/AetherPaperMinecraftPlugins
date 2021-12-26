package aetherial.aether.essentials.teleportation.command.home;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.Common;
import aetherial.aether.essentials.teleportation.TpRegistration;
import aetherial.aether.essentials.wrapper.CommandWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static aetherial.aether.essentials.chat.ChatColorFormatter.DEFAULT_PLAYER_COLOR_CODE;
import static aetherial.aether.essentials.chat.ChatColorFormatter.applyDefaultMessageColor;

@CommandTag(
    name = TpRegistration.HOME,
    usage = "/" + TpRegistration.HOME + " [string|nothing]",
    desc = "Teleport to your home",
    permission = Home.PERMISSION
)
public class Home extends CommandWrapper {

    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TpRegistration.HOME;
    public static final String DEFAULT_HOME_LABEL = "home";

    private final String notValidHomePrefix = applyDefaultMessageColor("Not a valid home: " + DEFAULT_PLAYER_COLOR_CODE);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        //noinspection DuplicatedCode No meaningful way to not duplicate
        Optional<Player> optionalPlayer = Common.verifyCommandSenderIsPlayer(commandSender, commandLabel);
        if (optionalPlayer.isEmpty() || !Common.verifyUpToNumberOfArgs(commandSender, args, 1)) {
            return false;
        }
        Player player = optionalPlayer.get();
        String homeLabel = args.length > 0 ? args[0] : DEFAULT_HOME_LABEL;

        Optional<Location> optionalLocation = HomeStorage.getInstance().getHome(player, command.getName());
        if (optionalLocation.isEmpty()) {
            commandSender.sendMessage(notValidHomePrefix + homeLabel);
            return true;
        }

        player.teleport(optionalLocation.get());

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return HomeStorage.getInstance().verifyIsPlayerOnTabComplete(commandSender, commandLabel, args);
    }

}
