package aetherial.aether.essentials.teleportation.command.home;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.Common;
import aetherial.aether.essentials.exception.AlreadyInitialized;
import aetherial.aether.essentials.exception.NotInitialized;
import aetherial.aether.essentials.util.persistence.Persistence;
import aetherial.aether.essentials.util.persistence.yaml.LabeledLocationYaml;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
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

    private final Logger log = LogManager.getLogger(AetherEssentials.PLUGIN_NAME);

    // PlayerUUID -> (homeName -> Location)
    private final Map<String, Map<String, Location>> allPlayerHomes = new ConcurrentHashMap<>();

    private HomeStorage(JavaPlugin plugin) {
        loadSavedHomes(plugin);
    }

    private void loadSavedHomes(JavaPlugin plugin) {
        File homesSubfolder = Persistence.getInstance().getDataSubfolder(HOMES_SUBFOLDER);
        Optional<File[]> optional = Optional.ofNullable(
            homesSubfolder.listFiles((dir, name) -> name.substring(name.lastIndexOf('.'))
                .equals(Persistence.YAML_FILE_EXT))
        );
        if (optional.isEmpty()) {
            log.info("Failed to load saved homes");
            return;
        }
        File[] files = optional.get();
        if (files.length == 0) {
            return;
        }

        for (File file : files) {
            Server server = plugin.getServer();
            Optional<PlayerHomesYaml> optionalPlayerHomesYaml = Persistence.getInstance()
                .readYamlFile(file, PlayerHomesYaml.class);
            if (optionalPlayerHomesYaml.isEmpty()) {
                continue;
            }
            PlayerHomesYaml playerHomesYaml = optionalPlayerHomesYaml.get();

            String playerUUID = playerHomesYaml.getPlayerUUID();
            Map<String, Location> homes = getHomesInternal(playerUUID);

            for (LabeledLocationYaml labeledLocationYaml : playerHomesYaml.getHomes()) {
                homes.put(labeledLocationYaml.getLabel(), labeledLocationYaml.asLocation(server));
            }

            allPlayerHomes.put(playerHomesYaml.getPlayerUUID(), homes);
        }
    }

    /**
     * Set and save a home Location
     *
     * @param player The Player setting the home
     * @param label  The name of the home Location
     * @return The location of the home if set successfully, otherwise empty if something went wrong
     */
    public Optional<Location> setHome(Player player, String label) {
        Map<String, Location> playerHomes = getHomesInternal(player);
        Location location = player.getLocation();
        playerHomes.put(label, location);

        boolean saved = savePlayerHomes(player);
        if (!saved) {
            // If the file was not saved, do not cache the home
            playerHomes.remove(label);
            return Optional.empty();
        }

        return Optional.of(location);
    }

    /**
     * Delete a saved home Location
     *
     * @param player The Player removing the home
     * @param label  The name of the home Location
     * @return The Location of the home if remove was successful, otherwise empty if something went wrong
     */
    public Optional<Location> deleteHome(Player player, String label) {
        Map<String, Location> playerHomes = getHomesInternal(player);

        Optional<Location> optionalHome = Optional.ofNullable(playerHomes.remove(label));
        if (optionalHome.isEmpty()) {
            // home does not exist
            return Optional.empty();
        }
        Location location = optionalHome.get();

        boolean savedChanges = savePlayerHomes(player);
        if (!savedChanges) {
            playerHomes.put(label, location);
            return Optional.empty();
        }

        return Optional.of(location);
    }

    private boolean savePlayerHomes(Player player) {
        String playerUUID = player.getUniqueId().toString();
        return Persistence.getInstance()
            .writeFileYaml(HOMES_SUBFOLDER, playerUUID, new PlayerHomesYaml(playerUUID, getHomesInternal(playerUUID)));
    }

    private Map<String, Location> getHomesInternal(Player player) {
        String playerUUID = player.getUniqueId().toString();
        return getHomesInternal(playerUUID);
    }

    private Map<String, Location> getHomesInternal(String playerUUID) {
        // https://www.baeldung.com/java-map-computeifabsent This is a really fancy method
        return allPlayerHomes.computeIfAbsent(playerUUID, s -> new ConcurrentHashMap<>());
    }

    /**
     * Get a copy of the home Locations of a Player
     * <p>
     * Changes to the returned Map will only change the true Map if a Location
     * is modified.
     *
     * @param player The Player to get the homes of
     * @return The home Locations of the Player
     */
    public Map<String, Location> getHomes(Player player) {
        return new HashMap<>(getHomesInternal(player));
    }

    public List<String> getHomeNames(Player player) {
        return new ArrayList<>(getHomes(player).keySet());
    }

    /**
     * Get the Location of a Player's home
     *
     * @param player The Player to get the home Location of
     * @param label  The name of the home Location
     * @return A Location if the specified name has been set, otherwise empty
     */
    public Optional<Location> getHome(Player player, String label) {
        return Optional.ofNullable(getHomesInternal(player).get(label));
    }

    public List<String> onTabComplete(Player player, String partial) {
        List<String> homes = getHomeNames(player);
        List<String> possibleCompletes = new ArrayList<>();

        for (String warp : homes) {
            if (warp.startsWith(partial)) {
                possibleCompletes.add(warp);
            }
        }

        return possibleCompletes;
    }

    public List<String> verifyIsPlayerOnTabComplete(CommandSender commandSender, String commandLabel, String[] args) {
        Optional<Player> optionalPlayer = Common.verifyCommandSenderIsPlayer(commandSender, commandLabel);
        if (optionalPlayer.isEmpty()) {
            return Collections.emptyList();
        }
        Player player = optionalPlayer.get();

        return args.length == 1 ? onTabComplete(player, args[0]) : Collections.emptyList();
    }

}
