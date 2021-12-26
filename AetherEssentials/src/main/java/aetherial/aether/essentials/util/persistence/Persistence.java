package aetherial.aether.essentials.util.persistence;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.exception.AlreadyInitialized;
import aetherial.aether.essentials.exception.ConfigException;
import aetherial.aether.essentials.exception.NotInitialized;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

public class Persistence {

    private static Persistence instance;
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

    //==========================================================================

    private final Logger log = LogManager.getLogger(AetherEssentials.PLUGIN_NAME);
    private final File dataFolder;

    private Persistence(JavaPlugin plugin) {
        dataFolder = plugin.getDataFolder();
    }

    /**
     * @return This plugin's data folder
     */
    public File getDataFolder() {
        return dataFolder;
    }

    /**
     * Get a subfolder in this plugin's data folder.
     * <p>
     * Will attempt to create the subfolder if it does not already exist.
     *
     * @param subfolder The name of the subfolder in this plugin's data folder
     * @return The subfolder in this plugin's data folder
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
     * Parse the YAML data from the given file
     * <p>
     * The given Class type MUST:
     * <ul>
     *     <li>Be a one-to-one mapping of the data stored in the file</li>
     *     <li>Have a no-argument constructor</li>
     *     <li>Contain only primitives, Maps, and or Classes that follow these same rules</li>
     *     <ul>
     *         <li>Strings are considered a primitive for this</li>
     *     </ul>
     * </ul>
     * <p>
     * Will create and return an instance of the given Class that represents the data.
     *
     * @param subfolder The subfolder in this plugin's data folder
     * @param filename  The name of the file in the subfolder without the extension
     * @param type      The Class type to instantiate and fill with the data from the file
     * @return The YAML parsed data from the file as the specified type
     */
    public <T> Optional<T> readYamlFile(String subfolder, String filename, Class<T> type) {
        File file = new File(getDataSubfolder(subfolder), filename + YAML_FILE_EXT);
        return readYamlFile(file, type);
    }

    /**
     * Parse the YAML data from the given file
     * <p>
     * The given Class type MUST:
     * <ul>
     *     <li>Be a one-to-one mapping of the data stored in the file</li>
     *     <li>Have a no-argument constructor</li>
     *     <li>Contain only primitives, Maps, and or Classes that follow these same rules</li>
     *     <ul>
     *         <li>Strings are considered a primitive for this</li>
     *     </ul>
     * </ul>
     * <p>
     * Will create and return an instance of the given Class that represents the data.
     *
     * @param filename The name of the file in this plugin's data folder without the extension
     * @param type     The Class type to instantiate and fill with the data from the file
     * @return The YAML parsed data from the file as the specified type
     */
    public <T> Optional<T> readYamlFile(String filename, Class<T> type) {
        File file = new File(dataFolder, filename + YAML_FILE_EXT);
        return readYamlFile(file, type);
    }

    /**
     * Parse the YAML data from the given file
     * <p>
     * The given Class type MUST:
     * <ul>
     *     <li>Be a one-to-one mapping of the data stored in the file</li>
     *     <li>Have a no-argument constructor</li>
     *     <li>Contain only primitives, Maps, and or Classes that follow these same rules</li>
     *     <ul>
     *         <li>Strings are considered a primitive for this</li>
     *     </ul>
     * </ul>
     * <p>
     * Will create and return an instance of the given Class that represents the data.
     *
     * @param file The File to read from
     * @param type The Class to instantiate and fill with the data from the file
     * @return The YAML parsed data from the file as the specified type
     */
    public <T> Optional<T> readYamlFile(File file, Class<T> type) {
        T data = null;
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Yaml yaml = new Yaml(new Constructor(type));
                data = yaml.loadAs(reader, type);
            }
            // FileReader could fail with IOException, Yaml could fail with different RuntimeException subtypes
            catch (IOException | RuntimeException e) {
                log.error("Failed to parse file: {}\n{}", file.getPath(), e.getMessage());
            }
        } else {
            log.warn("File does not exist: {}", file.getPath());
        }

        return Optional.ofNullable(data);
    }

    /**
     * Writes a data Class to a file in the subfolder of this plugin's data folder
     * <p>
     * If the file does not already exist, it will be created.
     * <p>
     * The given Class type MUST:
     * <ul>
     *     <li>Be a one-to-one mapping of the data stored in the file</li>
     *     <li>Have a no-argument constructor</li>
     *     <li>Contain only primitives, Maps, and or Classes that follow these same rules</li>
     *     <ul>
     *         <li>Strings are considered a primitive for this</li>
     *     </ul>
     * </ul>
     *
     * @param subfolder The subfolder in this plugin's data folder
     * @param filename  The name of the file to write to
     * @param data      The data Class to write
     * @return True if the file was written to, otherwise false
     */
    public <T> boolean writeFileYaml(String subfolder, String filename, T data) {
        File file = new File(getDataSubfolder(subfolder), filename + YAML_FILE_EXT);
        return writeFileYaml(file, data);
    }

    /**
     * Writes a data Class to a file in this plugin's data folder
     * <p>
     * If the file does not already exist, it will be created.
     * <p>
     * The given Class type MUST:
     * <ul>
     *     <li>Be a one-to-one mapping of the data stored in the file</li>
     *     <li>Have a no-argument constructor</li>
     *     <li>Contain only primitives, Maps, and or Classes that follow these same rules</li>
     *     <ul>
     *         <li>Strings are considered a primitive for this</li>
     *     </ul>
     * </ul>
     *
     * @param filename The name of the file to write to
     * @param data     The data Class to write
     * @return True if the file was written to, otherwise false
     */
    public <T> boolean writeFileYaml(String filename, T data) {
        File file = new File(dataFolder, filename + YAML_FILE_EXT);
        return writeFileYaml(file, data);
    }

    /**
     * Write a data Class to a File
     * <p>
     * If the file does not already exist, it will be created.
     * <p>
     * The given Class type MUST:
     * <ul>
     *     <li>Be a one-to-one mapping of the data stored in the file</li>
     *     <li>Have a no-argument constructor</li>
     *     <li>Contain only primitives, Maps, and or Classes that follow these same rules</li>
     *     <ul>
     *         <li>Strings are considered a primitive for this</li>
     *     </ul>
     * </ul>
     *
     * @param file The File to write to
     * @param data The data Class to write
     * @return True if the file was written to, otherwise false
     */
    public <T> boolean writeFileYaml(File file, T data) {
        try (FileWriter writer = new FileWriter(file)) {
            Yaml yaml = new Yaml();
            String raw = yaml.dumpAs(data, Tag.MAP, DumperOptions.FlowStyle.BLOCK);
            writer.write(raw);
            writer.flush();
        } catch (IOException | RuntimeException e) {
            log.error("Failed to write to file: {}", file.getPath());
            return false;
        }
        return true;
    }

    /**
     * @param subfolder The subfolder in this plugin's data folder
     * @param filename  The name of the file in the subfolder including the extension
     * @return The File in the subfolder
     */
    public File getFile(String subfolder, String filename) {
        return new File(getDataSubfolder(subfolder), filename);
    }

    /**
     * @param filename The name of the file in this plugin's data folder including the extension
     * @return The File in this plugin's data folder
     */
    public File getFile(String filename) {
        return new File(dataFolder, filename);
    }

    /**
     * Delete a YAML file from a subfolder
     *
     * @param subfolder The subfolder in this plugin's data folder
     * @param filename  The name of the file without the extension
     * @return True if the File was deleted or does not exist, otherwise false
     */
    public boolean deleteYamlFile(String subfolder, String filename) {
        File file = new File(getDataSubfolder(subfolder), filename + YAML_FILE_EXT);
        return deleteFile(file);
    }

    /**
     * Delete a YAML file in this plugin's data folder
     *
     * @param filename The name of the file without the extension
     * @return True if the File was deleted or does not exist, otherwise false
     */
    public boolean deleteYamlFile(String filename) {
        File file = new File(dataFolder, filename + YAML_FILE_EXT);
        return deleteFile(file);
    }

    /**
     * Delete a File
     *
     * @param file The File to delete
     * @return True if the File was deleted or does not exist, otherwise false
     */
    public boolean deleteFile(File file) {
        if (!file.exists()) {
            return true;
        }

        try {
            Files.delete(file.toPath());
            return true;
        } catch (IOException e) {
            log.error("Failed to delete file: {}", file.getPath());
            return false;
        }
    }


    /**
     * Read data from a Yaml file.
     *
     * @param subfolder The subfolder in this plugin's dataFolder
     * @param filename  The file name in the subfolder without the extension
     * @return The data stored in the file
     * @deprecated (Use the new readYamlFile methods)
     */
    @Deprecated(since = "1.0.0-SNAPSHOT")
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
     * @deprecated (Use the new readYamlFile methods)
     */
    @Deprecated(since = "1.0.0-SNAPSHOT")
    public Optional<Map<String, Object>> readYamlFile(File file) {
        if (!file.exists()) {
            String message = String.format("File does not exist: %s", file.getPath());
            log.error(message);
        }

        Yaml yaml = new Yaml();
        Map<String, Object> data = null;
        try {
            data = yaml.load(new FileReader(file));
        } catch (FileNotFoundException e) {
            String message = String.format("Failed to read from file: %s", file.getPath());
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
     * @deprecated (Use the new writeYamlFile methods)
     */
    @Deprecated(since = "1.0.0-SNAPSHOT")
    public boolean writeFileYaml(String subfolder, String filename, Map<String, ?> data, boolean create) {
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
            String message = String.format("Failed to write to file: %s", file.getPath());
            log.error(message);
            return false;
        }

        return true;
    }

    /**
     * Get a Yaml file from a subfolder.
     *
     * @param subfolder The subfolder in this plugin's dataFolder
     * @param filename  The name of the file without the extension
     * @param create    True to create the file if it does not exist, otherwise false
     * @return The Yaml file
     * @deprecated (Use the new getFile methods)
     */
    @Deprecated(since = "1.0.0-SNAPSHOT")
    public Optional<File> getYamlFile(String subfolder, String filename, boolean create) {
        File file = new File(getDataSubfolder(subfolder), filename + YAML_FILE_EXT);

        if (!file.exists() && create) {
            String message = String.format("Failed to create file: %s", file.getPath());
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
            String message = String.format("Files does not exist: %s", file.getPath());
            log.error(message);
            return Optional.empty();
        }

        return Optional.of(file);
    }

}
