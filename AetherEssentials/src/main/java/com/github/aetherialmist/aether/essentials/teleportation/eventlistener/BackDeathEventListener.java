package com.github.aetherialmist.aether.essentials.teleportation.eventlistener;

import com.github.aetherialmist.aether.essentials.teleportation.command.Back;
import com.github.aetherialmist.aether.essentials.teleportation.persistence.TpHistoryTracker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BackDeathEventListener implements Listener {

    @EventHandler
    public void backOnDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        if (!(player.hasPermission(Back.PERMISSION_ON_DEATH))) {
            return;
        }

        TpHistoryTracker.getInstance().updateBeforeLocation(player, player.getLocation());
    }

}
