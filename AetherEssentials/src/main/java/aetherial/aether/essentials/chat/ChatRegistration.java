package aetherial.aether.essentials.chat;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatRegistration {

    public ChatRegistration(JavaPlugin plugin) {
        registerColorFormatterEvents(plugin);
    }

    public void registerColorFormatterEvents(JavaPlugin plugin) {
        PluginManager manager = plugin.getServer().getPluginManager();
        manager.registerEvents(new ChatColorEventListener(), plugin);
    }

}
