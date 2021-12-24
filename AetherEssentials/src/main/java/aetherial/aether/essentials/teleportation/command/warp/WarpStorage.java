package aetherial.aether.essentials.teleportation.command.warp;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.exception.AlreadyInitialized;
import aetherial.aether.essentials.exception.NotInitialized;
import aetherial.aether.essentials.teleportation.command.LocationMapConverter;
import aetherial.aether.essentials.util.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WarpStorage {

    private static WarpStorage instance;
    private static final Map<String, Location> warpLocations = new ConcurrentHashMap<>();
    private static final String WARPS_SUBFOLDER = "warps";

    public static WarpStorage getInstance() {
        if (instance == null) {
            throw new NotInitialized("WarpStorage has not been initialized");
        }
        return instance;
    }

    public static void init(JavaPlugin plugin) {
        if (instance != null) {
            throw new AlreadyInitialized("WarpStorage has already been initialized");
        }
        instance = new WarpStorage(plugin);
    }

    private final Logger log = LogManager.getLogger(AetherEssentials.PLUGIN_NAME);
    private final File warpsFolder;

    private WarpStorage(JavaPlugin plugin) {
        warpsFolder = Persistence.getInstance().getDataSubfolder(WARPS_SUBFOLDER);
        loadSavedWarps(plugin);
    }

    private void loadSavedWarps(JavaPlugin plugin) {
        Optional<File[]> optional = Optional.ofNullable(
            warpsFolder.listFiles((dir, name) -> name.substring(name.lastIndexOf('.')).equals(Persistence.YAML_FILE_EXT))
        );
        if (optional.isEmpty()) {
            log.info("No saved warps to load");
            return;
        }
        File[] files = optional.get();

        for (File file : files) {
            Optional<Map<String, Object>> optionalData = Persistence.getInstance().readYamlFile(file);
            if (optionalData.isEmpty()) {
                String message = String.format("Failed to load saved Warp with file name: %s", file.getName());
                log.warn(message);
                continue;
            }
            Map<String, Object> data = optionalData.get();

            Location location = LocationMapConverter.fromMap(plugin.getServer(), data);
            String warpName = (String) data.get(LocationMapConverter.NAME_DATA);

            warpLocations.put(warpName, location);
        }
    }

    public List<String> getWarpLocationNames() {
        return new ArrayList<>(warpLocations.keySet());
    }

    public Optional<Location> getWarpLocation(String warpName) {
        return Optional.ofNullable(warpLocations.get(warpName));
    }

    public Optional<Location> setWarpLocation(String warpName, Location location) {
        boolean saved = saveWarp(warpName, location);
        if (!saved) {
            return Optional.empty();
        }
        warpLocations.put(warpName, location);

        return Optional.of(location);
    }

    private boolean saveWarp(String warpName, Location location) {
        Map<String, Object> data = LocationMapConverter.toMap(warpName, location);

        return Persistence.getInstance().writeFileYaml(WARPS_SUBFOLDER, warpName, data, true);
    }

    public Optional<Location> deleteWarpLocation(String warpName) {
        boolean deleted = deleteWarp(warpName);
        if (!deleted) {
            return Optional.empty();
        }
        Location location = warpLocations.remove(warpName);
        return Optional.ofNullable(location);
    }

    private boolean deleteWarp(String warpName) {
        return Persistence.getInstance().deleteYamlFile(WARPS_SUBFOLDER, warpName);
    }

    public List<String> onTabComplete(String arg) {
        List<String> allWarps = new ArrayList<>(getWarpLocationNames());
        List<String> possibleCompletes = new ArrayList<>();

        for (String warp : allWarps) {
            if (warp.startsWith(arg)) {
                possibleCompletes.add(warp);
            }
        }

        return possibleCompletes;
    }

}
