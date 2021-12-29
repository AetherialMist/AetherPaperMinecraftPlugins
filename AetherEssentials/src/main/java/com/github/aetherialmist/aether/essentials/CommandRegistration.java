package com.github.aetherialmist.aether.essentials;

import com.github.aetherialmist.aether.essentials.exception.AlreadyInitialized;
import com.github.aetherialmist.aether.essentials.wrapper.CommandWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Handles registering a command and setting its {@link org.bukkit.command.CommandExecutor} and {@link org.bukkit.command.TabCompleter}
 * <p>
 * The {@link #init(JavaPlugin)} must be called at the startup of this plugin
 * before any calls to {@link #registerCommand(String, CommandWrapper)} are
 * called.
 */
public class CommandRegistration {

    private static CommandRegistration instance;

    /**
     * Initialize the stored instance of this.
     *
     * @param plugin This plugin
     */
    public static void init(JavaPlugin plugin) {
        if (instance != null) {
            throw new AlreadyInitialized(CommandRegistration.class);
        }
        instance = new CommandRegistration(plugin);
    }

    /**
     * @param commandLabel   The label of the command, what follows directly after the command prefix '/'
     * @param commandWrapper The command's executor and tab completer
     */
    public static void registerCommand(String commandLabel, CommandWrapper commandWrapper) {
        instance.registerCommandInternal(commandLabel, commandWrapper);
    }

    private final Logger log = LogManager.getLogger(AetherEssentials.PLUGIN_NAME);
    private final JavaPlugin plugin;

    private CommandRegistration(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private void registerCommandInternal(String commandLabel, CommandWrapper commandWrapper) {
        PluginCommand command = plugin.getCommand(commandLabel);
        if (command != null) {
            command.setExecutor(commandWrapper);
            command.setTabCompleter(commandWrapper);
        } else {
            log.error("Failed to register command: /{}", commandLabel);
        }
    }

}
