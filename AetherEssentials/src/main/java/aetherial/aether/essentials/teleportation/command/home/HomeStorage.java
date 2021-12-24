package aetherial.aether.essentials.teleportation.command.home;

import aetherial.aether.essentials.exception.AlreadyInitialized;
import aetherial.aether.essentials.exception.NotInitialized;
import aetherial.aether.essentials.util.Persistence;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HomeStorage {

    private static HomeStorage instance;
    private static final String HOMES_SUBFOLDER = "homes";

    public static HomeStorage getInstance() {
        if (instance == null) {
            throw new NotInitialized("Home Storage has not been initialized");
        }
        return instance;
    }

    public static void init(JavaPlugin plugin) {
        if (instance != null) {
            throw new AlreadyInitialized("Home Storage has already been initialized");
        }
        instance = new HomeStorage(plugin);
    }

    private final JavaPlugin plugin;
    private final File homesSubfolder;

    // PlayerUUID -> (homeName -> (key -> value))
    private final Map<String, Map<String, Map<String, Object>>> locations = new ConcurrentHashMap<>();

    private HomeStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        this.homesSubfolder = Persistence.getInstance().getDataSubfolder(HOMES_SUBFOLDER);
    }


}
