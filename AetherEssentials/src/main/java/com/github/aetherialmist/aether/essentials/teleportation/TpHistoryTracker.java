package com.github.aetherialmist.aether.essentials.teleportation;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class TpHistoryTracker {

    private static TpHistoryTracker instance;

    public static TpHistoryTracker getInstance() {
        if (instance == null) {
            instance = new TpHistoryTracker();
        }
        return instance;
    }

    private final Map<Player, Location> beforeRecentTpLocation = new ConcurrentHashMap<>();

    private TpHistoryTracker() {
    }

    /**
     * Sets the back location to where the Player currently is
     *
     * @param player The player to update their back location
     */
    public void updateBeforeLocation(Player player) {
        this.updateBeforeLocation(player, player.getLocation());
    }

    public void updateBeforeLocation(Player player, Location location) {
        this.beforeRecentTpLocation.put(player, location);
    }

    public Optional<Location> getBeforeTpLocation(@NotNull Player player) {
        return Optional.ofNullable(this.beforeRecentTpLocation.get(player));
    }

}
