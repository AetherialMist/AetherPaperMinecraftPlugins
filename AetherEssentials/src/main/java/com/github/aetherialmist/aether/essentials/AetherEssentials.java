package com.github.aetherialmist.aether.essentials;

import com.github.aetherialmist.aether.essentials.chat.ChatRegistration;
import com.github.aetherialmist.aether.essentials.home.HomeRegistrar;
import com.github.aetherialmist.aether.essentials.teleportation.TeleportationRegistrar;
import com.github.aetherialmist.aether.essentials.util.persistence.Persistence;
import aetherial.spigot.plugin.annotation.permission.PermissionTag;
import aetherial.spigot.plugin.annotation.plugin.ApiVersion;
import aetherial.spigot.plugin.annotation.plugin.Author;
import aetherial.spigot.plugin.annotation.plugin.Description;
import aetherial.spigot.plugin.annotation.plugin.Plugin;
import com.github.aetherialmist.aether.essentials.vanilla.VanillaRegistrar;
import com.github.aetherialmist.aether.essentials.warp.WarpRegistrar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;

@Plugin(name = AetherEssentials.PLUGIN_NAME, version = "0.1.0")
@Description("An alternative to EssentialsX. Useful commands.")
@Author("AetherialMist")
@ApiVersion(value = "1.18")
@PermissionTag(name = AetherEssentials.PERMISSION_BASE + "*", desc = "All Aether Essentials permissions")
public class AetherEssentials extends JavaPlugin {

    public static final String PLUGIN_NAME = "AetherEssentials";
    public static final String PERMISSION_BASE = "aether.essentials.";
    public static final String COMMAND_PREFIX = "/";

    private final Logger log = LogManager.getLogger(AetherEssentials.PLUGIN_NAME);

    // Used to keep the object instance alive by keeping a reference
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private ChatRegistration chatRegistration;

    /**
     * Called when the server starts
     */
    @Override
    public void onEnable() {
        // SnakeYaml breaks if you don't set the ClassLoader...
        Thread.currentThread().setContextClassLoader(this.getClassLoader());

        CommandRegistration.init(this);
        EventRegistration.init(this);
        Common.init(this);
        Persistence.init(this);

        initModules();
        chatRegistration = new ChatRegistration(this);

        log.info("Finished initializing");
    }

    private void initModules() {
        TeleportationRegistrar.init();
        HomeRegistrar.init();
        WarpRegistrar.init();
        VanillaRegistrar.init();
    }

    /**
     * Called when the server stops
     */
    @Override
    public void onDisable() {
        // Placeholder
    }
}
