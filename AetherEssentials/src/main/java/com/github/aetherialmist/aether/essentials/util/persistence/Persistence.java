package com.github.aetherialmist.aether.essentials.util.persistence;

import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.exception.AlreadyInitialized;
import com.github.aetherialmist.aether.essentials.exception.FailedInitialize;
import com.github.aetherialmist.aether.essentials.exception.NotInitialized;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.*;
import java.nio.file.Files;
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
            throw new FailedInitialize(message);
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

}
