package aetherial.aether.essentials.teleportation.command;

import org.bukkit.Location;
import org.bukkit.Server;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LocationYamlConverter {

    public static final String NAME_DATA = "name";

    private static final String WORLD_UUID = "world-uuid";
    private static final String X_DATA = "x";
    private static final String Y_DATA = "y";
    private static final String Z_DATA = "z";
    private static final String PITCH_DATA = "pitch";
    private static final String YAW_DATA = "yaw";

    private LocationYamlConverter() {

    }

    public static Map<String, Object> toYamlMap(String label, Location location) {
        Map<String, Object> data = new HashMap<>();
        data.put(NAME_DATA, label);
        data.put(WORLD_UUID, location.getWorld().getUID().toString());
        data.put(X_DATA, location.getX());
        data.put(Y_DATA, location.getY());
        data.put(Z_DATA, location.getZ());
        data.put(PITCH_DATA, location.getPitch());
        data.put(YAW_DATA, location.getYaw());

        return data;
    }

    public static Location fromYamlMap(Server server, Map<String, Object> data) {
        UUID worldUUID = UUID.fromString((String) data.get(WORLD_UUID));
        // Casting the float data to a string before parsing does not work, but string concat does.
        return new Location(
            server.getWorld(worldUUID),
            (double) data.get(X_DATA),
            (double) data.get(Y_DATA),
            (double) data.get(Z_DATA),
            Float.parseFloat("" + data.get(YAW_DATA)),
            Float.parseFloat("" + data.get(PITCH_DATA))
        );
    }

}
