package aetherial.aether.essentials.util;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.exception.AlreadyInitialized;
import aetherial.aether.essentials.exception.ConfigException;
import aetherial.aether.essentials.exception.NotInitialized;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

public class Persistence {

    private static Persistence instance;
    private static final String LOG_PREFIX = "[Persistence] ";
    public static final String YAML_FILE_EXT = ".yml";

    public static void init(JavaPlugin plugin) {
        if (instance != null) {
            throw new AlreadyInitialized("Persistence has already been initialized");
        }
        instance = new Persistence(plugin);
    }

    public static Persistence getInstance() {
        if (instance == null) {
            throw new NotInitialized("Persistence has not been initialized");
        }
        return instance;
    }

    private final Logger log = LogManager.getLogger(AetherEssentials.PLUGIN_NAME);
    private final File dataFolder;

    private Persistence(JavaPlugin plugin) {
        dataFolder = plugin.getDataFolder();
    }

    /**
     * Get a subfolder from this plugin's dataFolder.
     * <p>
     * Will attempt to create the subfolder if it does not already exist
     *
     * @param subfolder The name of the subfolder in this plugin's dataFolder
     * @return The subfolder in this plugin's dataFolder
     */
    public File getDataSubfolder(String subfolder) {
        File folder = new File(dataFolder, subfolder);
        boolean folderExists = folder.exists();

        if (!folderExists) {
            folderExists = folder.mkdirs();
        }

        if (!folderExists) {
            String message = String.format("Failed to initialize data subfolder: %s", subfolder);
            throw new ConfigException(message);
        }

        return folder;
    }

    /**
     * Read data from a Yaml file.
     *
     * @param subfolder The subfolder in this plugin's dataFolder
     * @param filename  The file name in the subfolder without the extension
     * @return The data stored in the file
     */
    public Optional<Map<String, Object>> readYamlFile(String subfolder, String filename) {
        Optional<File> optional = getYamlFile(subfolder, filename, false);
        if (optional.isEmpty()) {
            return Optional.empty();
        }

        return readYamlFile(optional.get());
    }

    /**
     * Read data from a Yaml file.
     *
     * @param file The file to read from
     * @return The data stored in the file
     */
    public Optional<Map<String, Object>> readYamlFile(File file) {
        if (!file.exists()) {
            String message = String.format("%sFile does not exist: %s", LOG_PREFIX, file.getPath());
            log.error(message);
        }

        Yaml yaml = new Yaml();
        Map<String, Object> data = null;
        try {
            data = yaml.load(new FileReader(file));
        } catch (FileNotFoundException e) {
            String message = String.format("%sFailed to read from file: %s", LOG_PREFIX, file.getPath());
            log.error(message);
        }
        return Optional.ofNullable(data);
    }

    /**
     * Writes to a Yaml file.
     *
     * @param subfolder The subfolder in this plugin's dataFolder
     * @param filename  The file name in the subfolder without the extension
     * @param data      The Yaml data to write
     * @param create    True to create the file if it does not already exist, otherwise false
     * @return True if the file was written to, otherwise false
     */
    public boolean writeFileYaml(String subfolder, String filename, Map<String, Object> data, boolean create) {
        Optional<File> optional = getYamlFile(subfolder, filename, create);
        if (optional.isEmpty()) {
            return false;
        }
        File file = optional.get();

        try (Writer writer = new FileWriter(file)) {
            Yaml yaml = new Yaml();
            String raw = yaml.dumpAs(data, Tag.MAP, DumperOptions.FlowStyle.BLOCK);
            writer.write(raw);
            writer.flush();
        } catch (IOException e) {
            String message = String.format("%sFailed to write to file: %s", LOG_PREFIX, file.getPath());
            log.error(message);
            return false;
        }

        return true;
    }

    /**
     * Get a Yaml file from a subfolder.
     *
     * @param subfolder The subfolder in this plugin's dataFolder
     * @param filename  The file name in the subfolder without the extension
     * @return The Yaml file
     */
    public Optional<File> getYamlFile(String subfolder, String filename) {
        return getYamlFile(subfolder, filename, false);
    }

    /**
     * Get a Yaml file from a subfolder.
     *
     * @param subfolder The subfolder in this plugin's dataFolder
     * @param filename  The name of the file without the extension
     * @param create    True to create the file if it does not exist, otherwise false
     * @return The Yaml file
     */
    public Optional<File> getYamlFile(String subfolder, String filename, boolean create) {
        File file = new File(getDataSubfolder(subfolder), filename + YAML_FILE_EXT);

        if (!file.exists() && create) {
            String message = String.format("%sFailed to create file: %s", LOG_PREFIX, file.getPath());
            Path path = file.toPath();
            try {
                Files.createFile(path);
            } catch (IOException e) {
                log.error(message);
                return Optional.empty();
            }
            file = new File(path.toUri());
            if (!file.exists()) {
                log.error(message);
                return Optional.empty();
            }
        } else if (!file.exists()) {
            String message = String.format("%sFiles does not exist: %s", LOG_PREFIX, file.getPath());
            log.error(message);
            return Optional.empty();
        }

        return Optional.of(file);
    }

    /**
     * Delete a Yaml file from a subfolder.
     *
     * @param subfolder The subfolder in this plugin's dataFolder
     * @param filename  The name of the file without the extension
     * @return True if the file was deleted, otherwise false
     */
    public boolean deleteYamlFile(String subfolder, String filename) {
        File file = new File(getDataSubfolder(subfolder), filename +YAML_FILE_EXT);

        if (!file.exists()) {
            return true;
        }

        try {
            Files.delete(file.toPath());
            return true;
        } catch (IOException e) {
            log.error(String.format("%sFailed to delete file: %s", LOG_PREFIX, file.getPath()));
            return false;
        }
    }

}
