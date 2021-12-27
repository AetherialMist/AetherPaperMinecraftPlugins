package com.github.aetherialmist.aether.essentials.teleportation;

import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.teleportation.command.Back;
import com.github.aetherialmist.aether.essentials.teleportation.command.home.*;
import com.github.aetherialmist.aether.essentials.teleportation.command.tp.*;
import com.github.aetherialmist.aether.essentials.teleportation.command.warp.*;
import com.github.aetherialmist.aether.essentials.wrapper.CommandWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TpRegistration {

    private final Logger log = LogManager.getLogger(AetherEssentials.PLUGIN_NAME);

    public static final String BACK = "back";

    public static final String TP_REQUEST = "tpr";
    public static final String TP_REQUEST_HERE = "tprhere";
    public static final String TP_REQUEST_HERE_ALL = "tprhereall";

    public static final String TP_ACCEPT = "tpaccept";
    public static final String TP_DENY = "tpdeny";
    public static final String TP_CANCEL = "tpcancel";

    public static final String TP_TOGGLE = "tptoggle";

    public static final String TP_OVERRIDE = "tpo";
    public static final String TP_OVERRIDE_HERE = "tpohere";
    public static final String TP_OVERRIDE_HERE_ALL = "tpohereall";

    public static final String WARP = "warp";
    public static final String WARPS = "warps";
    public static final String WARP_INFO = "warpinfo";
    public static final String SET_WARP = "setwarp";
    public static final String DELETE_WARP = "delwarp";

    public static final String HOME = "home";
    public static final String HOMES = "homes";
    public static final String HOME_INFO = "homeinfo";
    public static final String SET_HOME = "sethome";
    public static final String DELETE_HOME = "delhome";

    private final JavaPlugin plugin;

    public TpRegistration(JavaPlugin plugin) {
        this.plugin = plugin;

        registerCommands();
        registerEventListeners();
    }

    public void registerCommands() {
        WarpStorage.init(plugin);
        HomeStorage.init(plugin);
        TpToggle.init();

        registerCommand(BACK, new Back());

        registerCommand(TP_REQUEST, new TpRequest());
        registerCommand(TP_REQUEST_HERE, new TpRequestHere());
        registerCommand(TP_REQUEST_HERE_ALL, new TpRequestHereAll());
        registerCommand(TP_ACCEPT, new TpAccept());
        registerCommand(TP_DENY, new TpDeny());
        registerCommand(TP_CANCEL, new TpCancel());
        registerCommand(TP_TOGGLE, TpToggle.getInstance());

        registerCommand(WARP, new Warp());
        registerCommand(WARPS, new Warps());
        registerCommand(WARP_INFO, new WarpInfo());
        registerCommand(SET_WARP, new SetWarp());
        registerCommand(DELETE_WARP, new DeleteWarp());

        registerCommand(HOME, new Home());
        registerCommand(HOMES, new Homes());
        registerCommand(HOME_INFO, new HomeInfo());
        registerCommand(SET_HOME, new SetHome());
        registerCommand(DELETE_HOME, new DeleteHome());
    }

    private void registerCommand(String commandLabel, CommandWrapper commandWrapper) {
        PluginCommand command = plugin.getCommand(commandLabel);
        if (command != null) {
            command.setExecutor(commandWrapper);
            command.setTabCompleter(commandWrapper);
        } else {
            log.error("Failed to register command: /{}", commandLabel);
        }
    }

    private void registerEventListeners() {
        PluginManager manager = plugin.getServer()
            .getPluginManager();

        manager.registerEvents(new DeathEventListener(), plugin);
    }

}
