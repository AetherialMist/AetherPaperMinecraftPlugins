package aetherial.aether.essentials.teleportation.command.home;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.exception.AlreadyInitialized;
import aetherial.aether.essentials.exception.NotInitialized;
import aetherial.aether.essentials.teleportation.command.LocationMapConverter;
import aetherial.aether.essentials.util.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

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

    private final Logger log = LogManager.getLogger(AetherEssentials.PLUGIN_NAME);
    private final JavaPlugin plugin;
    private final File homesSubfolder;

    // PlayerUUID -> (homeName -> (key -> value))
    private final Map<String, Map<String, Map<String, Object>>> allPlayerHomes = new ConcurrentHashMap<>();

    private HomeStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        this.homesSubfolder = Persistence.getInstance().getDataSubfolder(HOMES_SUBFOLDER);
    }

    /**
     * Sets and saves a Player's home
     *
     * @param player
     * @param label
     * @return
     */
    public boolean setHome(Player player, String label) {
        Map<String, Map<String, Object>> playerHomes = getPlayerHomes(player);
        Map<String, Object> data = LocationMapConverter.toMap(label, player.getLocation());

        playerHomes.put(label, data);
        boolean saved = savePlayerHomes(player.getUniqueId().toString());

        return saved && playerHomes.containsKey(label);
    }

    public boolean savePlayerHomes(String playerUUID) {
        return Persistence.getInstance().writeFileYaml(HOMES_SUBFOLDER, playerUUID, allPlayerHomes.get(playerUUID), true);
    }

    public Map<String, Map<String, Object>> getPlayerHomes(Player player) {
        String playerUUID = player.getUniqueId().toString();

        // https://www.baeldung.com/java-map-computeifabsent This is a really fancy method
        return allPlayerHomes.computeIfAbsent(playerUUID, s -> new ConcurrentHashMap<>());
    }


}
