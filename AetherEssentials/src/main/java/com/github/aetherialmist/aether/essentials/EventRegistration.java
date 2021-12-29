package com.github.aetherialmist.aether.essentials;

import com.github.aetherialmist.aether.essentials.exception.AlreadyInitialized;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Handles registering an Event Listener
 */
public class EventRegistration {

    private static EventRegistration instance;

    /**
     * Initialize the instance of this for static method calls
     *
     * @param plugin This plugin
     */
    public static void init(JavaPlugin plugin) {
        if (instance != null) {
            throw new AlreadyInitialized(EventRegistration.class);
        }
        instance = new EventRegistration(plugin);
    }

    /**
     * @param listener The Listener to register
     */
    public static void registerEvents(Listener listener) {
        instance.registerEventsInternal(listener);
    }

    private final JavaPlugin plugin;
    private final PluginManager manager;

    private EventRegistration(JavaPlugin plugin) {
        this.plugin = plugin;
        this.manager = plugin.getServer().getPluginManager();
    }

    private void registerEventsInternal(Listener listener) {
        manager.registerEvents(listener, plugin);
    }

}
