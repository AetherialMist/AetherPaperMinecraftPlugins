package aetherial.aether.essentials;

import aetherial.aether.essentials.chat.ChatRegistration;
import aetherial.aether.essentials.teleportation.TpRegistration;
import aetherial.aether.essentials.util.Persistence;
import aetherial.spigot.plugin.annotation.permission.PermissionTag;
import aetherial.spigot.plugin.annotation.plugin.ApiVersion;
import aetherial.spigot.plugin.annotation.plugin.Author;
import aetherial.spigot.plugin.annotation.plugin.Description;
import aetherial.spigot.plugin.annotation.plugin.Plugin;
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

    @SuppressWarnings("unused")
    private final Logger log = LogManager.getLogger(AetherEssentials.PLUGIN_NAME);

    // Used to keep the object instance alive by keeping a reference
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private TpRegistration tpRegistration;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private ChatRegistration chatRegistration;

    /**
     * Called when the server starts
     */
    @Override
    public void onEnable() {
        Common.init(this);
        Persistence.init(this);
        tpRegistration = new TpRegistration(this);
        chatRegistration = new ChatRegistration(this);
    }

    /**
     * Called when the server stops
     */
    @Override
    public void onDisable() {
        // Placeholder
    }
}
