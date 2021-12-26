package com.github.aetherial.test.yaml.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Can NOT be an actual Java Record type.
 * MUST have a no-args constructor
 */
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

    public LocationYaml(String worldUUID, double x, double y, double z, float yaw, float pitch) {
        this.worldUUID = worldUUID;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Location getLocation(Server server) {
        return new Location(server.getWorld(UUID.fromString(worldUUID)), x, y, z, yaw, pitch);
    }

    public String toString() {
        return String.format("{WorldUUID: %s, X: %f, Y: %f, Z: %f, Yaw: %f, Pitch: %f}",
            worldUUID, x, y, z, yaw, pitch);
    }

}
