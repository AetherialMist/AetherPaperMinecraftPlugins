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

    public void updateBeforeLocation(Player player, Location location) {
        this.beforeRecentTpLocation.put(player, location);
    }

    public Optional<Location> getBeforeTpLocation(@NotNull Player player) {
        return Optional.ofNullable(this.beforeRecentTpLocation.get(player));
    }

}
