package aetherial.aether.essentials.teleportation.command;

import aetherial.aether.essentials.AetherEssentials;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.Server;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class LocationMapConverter {

    private static LocationMapConverter instance;

    public static LocationMapConverter getInstance() {
        if (instance == null) {
            instance = new LocationMapConverter();
        }
        return instance;
    }

    public static final String NAME_DATA = "label";

    private static final String WORLD_UUID = "world-uuid";
    private static final String X_DATA = "x";
    private static final String Y_DATA = "y";
    private static final String Z_DATA = "z";
    private static final String PITCH_DATA = "pitch";
    private static final String YAW_DATA = "yaw";

    public static Map<String, Object> toMap(String label, Location location) {
        return getInstance().instanceToMap(label, location);
    }

    public static Optional<Location> fromMap(Server server, Map<String, Object> data) {
        return getInstance().instanceFromMap(server, data);
    }

    private final Logger log = LogManager.getLogger(AetherEssentials.PLUGIN_NAME);

    private LocationMapConverter() {

    }

    public Map<String, Object> instanceToMap(String label, Location location) {
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

    public Optional<Location> instanceFromMap(Server server, Map<String, Object> data) {
        Location location = null;

        try {
            UUID worldUUID = UUID.fromString((String) data.get(WORLD_UUID));
            // Casting the float data to a string before parsing does not work, but string concat does.
            location = new Location(
                server.getWorld(worldUUID),
                (double) data.get(X_DATA),
                (double) data.get(Y_DATA),
                (double) data.get(Z_DATA),
                Float.parseFloat("" + data.get(YAW_DATA)),
                Float.parseFloat("" + data.get(PITCH_DATA))
            );
        } catch (RuntimeException e) {
            log.error("Failed to convert Map to Location");
        }
        return Optional.ofNullable(location);
    }

    public static String getLabel(Map<String, Object> data) {
        return (String) data.get(NAME_DATA);
    }

}
