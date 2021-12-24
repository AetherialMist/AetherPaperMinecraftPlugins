package aetherial.aether.essentials.teleportation.command;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.Common;
import aetherial.aether.essentials.teleportation.TpHistoryTracker;
import aetherial.aether.essentials.teleportation.TpRegistration;
import aetherial.aether.essentials.wrapper.CommandWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import aetherial.spigot.plugin.annotation.permission.PermissionTag;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static aetherial.aether.essentials.chat.ChatColorFormatter.applyDefaultMessageColor;

/**
 * Teleport to the location before the most recent TP
 * <p>
 * By setting all the message Strings as class variables, we create
 * them once. Instead of each time the command is used.
 */
@CommandTag(
    name = TpRegistration.BACK,
    usage = AetherEssentials.COMMAND_PREFIX + TpRegistration.BACK,
    desc = "Teleport to your previous location",
    permission = Back.PERMISSION
)
@PermissionTag(name = Back.PERMISSION_ON_DEATH, desc = "Death counts as last teleport location")
@PermissionTag(name = Back.PERMISSION_ON_TP, desc = "Can use back after any teleport")
public class Back extends CommandWrapper {

    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TpRegistration.BACK;
    public static final String PERMISSION_ON_DEATH = AetherEssentials.PERMISSION_BASE + TpRegistration.BACK + ".ondeath";
    public static final String PERMISSION_ON_TP = AetherEssentials.PERMISSION_BASE + TpRegistration.BACK + ".onteleport";

    private final String noBackLocation = applyDefaultMessageColor("No location to go back to");

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Optional<Player> optionalSender = Common.verifyCommandSenderIsPlayer(commandSender, label);
        if (optionalSender.isEmpty() || !Common.verifyNoArgs(commandSender, args)) {
            return false;
        }
        Player player = optionalSender.get();

        // Get the Location before the last TP
        TpHistoryTracker tracker = TpHistoryTracker.getInstance();
        Optional<Location> optionalLocation = tracker.getBeforeTpLocation(player);

        if (optionalLocation.isEmpty()) {
            player.sendMessage(noBackLocation);
            return true;
        }
        Location beforeLocation = optionalLocation.get();

        // Track the TP history
        if (player.hasPermission(PERMISSION_ON_TP)) {
            tracker.updateBeforeLocation(player, player.getLocation());
        }

        // Teleport the player
        player.teleport(beforeLocation);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] strings) {
        return Collections.emptyList();
    }

}
