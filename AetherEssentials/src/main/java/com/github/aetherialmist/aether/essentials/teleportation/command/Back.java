package com.github.aetherialmist.aether.essentials.teleportation.command;

import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.Common;
import com.github.aetherialmist.aether.essentials.teleportation.persistence.TpHistoryTracker;
import com.github.aetherialmist.aether.essentials.teleportation.TeleportationRegistrar;
import com.github.aetherialmist.aether.essentials.wrapper.CommandWrapper;
import com.github.aetherialmist.spigot.plugin.annotation.command.CommandTag;
import com.github.aetherialmist.spigot.plugin.annotation.permission.PermissionTag;
import com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Teleport to the location before the most recent TP
 * <p>
 * By setting all the message Strings as class variables, we create
 * them once. Instead of each time the command is used.
 */
@CommandTag(
    name = TeleportationRegistrar.BACK,
    usage = AetherEssentials.COMMAND_PREFIX + TeleportationRegistrar.BACK,
    desc = "Teleport to your previous location",
    permission = Back.PERMISSION
)
@PermissionTag(name = Back.PERMISSION_ON_DEATH, desc = "Death counts as last teleport location")
@PermissionTag(name = Back.PERMISSION_ON_TP, desc = "Can use back after any teleport")
public class Back extends CommandWrapper {

    /**
     * The permission associated with this command
     */
    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TeleportationRegistrar.BACK;

    /**
     * The permission for death to count as a back location
     */
    public static final String PERMISSION_ON_DEATH = AetherEssentials.PERMISSION_BASE + TeleportationRegistrar.BACK + ".ondeath";

    /**
     * The permission for teleporting to count as a back location
     */
    public static final String PERMISSION_ON_TP = AetherEssentials.PERMISSION_BASE + TeleportationRegistrar.BACK + ".onteleport";

    private final String noBackLocation = ChatColorFormatter.applyDefaultMessageColor("No location to go back to");

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        Optional<Player> optionalSender = Common.verifyCommandSenderIsPlayer(commandSender, commandLabel);
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
