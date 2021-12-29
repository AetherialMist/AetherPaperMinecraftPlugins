package com.github.aetherialmist.aether.essentials.warp;

import com.github.aetherialmist.aether.essentials.exception.AlreadyInitialized;
import com.github.aetherialmist.aether.essentials.warp.command.*;
import com.github.aetherialmist.aether.essentials.warp.persistence.WarpStorage;

import static com.github.aetherialmist.aether.essentials.CommandRegistration.registerCommand;

/**
 * Handles registering the warp module
 */
public class WarpRegistrar {

    private static WarpRegistrar instance;

    /**
     * Initialize this module and all of its dependencies
     */
    public static void init() {
        if (instance != null) {
            throw new AlreadyInitialized(WarpRegistrar.class);
        }
        WarpStorage.init();
        instance = new WarpRegistrar();
    }

    /**
     * The command label for the warp command
     */
    public static final String WARP = "warp";

    /**
     * The command label for the warps command
     */
    public static final String WARPS = "warps";

    /**
     * The command label for the warp-info command
     */
    public static final String WARP_INFO = "warpinfo";

    /**
     * The command label for the set-warp command
     */
    public static final String SET_WARP = "setwarp";

    /**
     * The command label for the delete-warp command
     */
    public static final String DELETE_WARP = "delwarp";

    private WarpRegistrar() {
        registerCommands();
    }

    private void registerCommands() {
        registerCommand(WarpRegistrar.WARP, new Warp());
        registerCommand(WarpRegistrar.WARPS, new Warps());
        registerCommand(WarpRegistrar.WARP_INFO, new WarpInfo());
        registerCommand(WarpRegistrar.SET_WARP, new SetWarp());
        registerCommand(WarpRegistrar.DELETE_WARP, new DeleteWarp());
    }

}
