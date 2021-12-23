package aetherial.aether.essentials.teleportation;

import aetherial.aether.essentials.teleportation.command.Back;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEventListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void backOnDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        if (!(player.hasPermission(Back.PERMISSION_ON_DEATH))) {
            return;
        }

        TpHistoryTracker.getInstance().updateBeforeLocation(player, player.getLocation());
    }

}
