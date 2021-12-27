package com.github.aetherialmist.aether.essentials.util.persistence.yaml;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Server;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class LocationYaml {

    private String worldUUID;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public LocationYaml(Location location) {
        this.worldUUID = location.getWorld().getUID().toString();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public Location asLocation(Server server) {
        return new Location(server.getWorld(UUID.fromString(worldUUID)), x, y, z, yaw, pitch);
    }

    public String toString() {
        return String.format("{WorldUUID: %s, X: %f, Y: %f, Z: %f, Yaw: %f, Pitch: %f}",
            worldUUID, x, y, z, yaw, pitch);
    }

}
