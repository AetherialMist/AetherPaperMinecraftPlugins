package com.github.aetherialmist.aether.essentials.teleportation.persistence;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks the location of a player before they teleport
 */
public class TpHistoryTracker {

    private static TpHistoryTracker instance;

    /**
     * @return The instance of this tracker
     */
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

    /**
     * Sets the back location of a Player
     *
     * @param player   The Player to update the previous location of
     * @param location The Location of the Player to store
     */
    public void updateBeforeLocation(Player player, Location location) {
        this.beforeRecentTpLocation.put(player, location);
    }

    /**
     * @param player The Player to query the before location of
     * @return The stored Location if it exists, otherwise empty
     */
    public Optional<Location> getBeforeTpLocation(@NotNull Player player) {
        return Optional.ofNullable(this.beforeRecentTpLocation.get(player));
    }

}
