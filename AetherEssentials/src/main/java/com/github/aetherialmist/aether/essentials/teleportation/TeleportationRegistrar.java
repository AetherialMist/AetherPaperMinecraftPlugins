package com.github.aetherialmist.aether.essentials.teleportation;

import com.github.aetherialmist.aether.essentials.exception.AlreadyInitialized;
import com.github.aetherialmist.aether.essentials.teleportation.command.*;
import com.github.aetherialmist.aether.essentials.teleportation.eventlistener.BackDeathEventListener;

import static com.github.aetherialmist.aether.essentials.CommandRegistration.registerCommand;
import static com.github.aetherialmist.aether.essentials.EventRegistration.registerEvents;

/**
 * Handles registering the Teleport module
 */
public class TeleportationRegistrar {

    private static TeleportationRegistrar instance;

    /**
     * Initialize this module and all of its dependencies
     */
    public static void init() {
        if (instance != null) {
            throw new AlreadyInitialized(TeleportationRegistrar.class);
        }
        instance = new TeleportationRegistrar();
    }

    /**
     * The command label for the back command
     */
    public static final String BACK = "back";

    /**
     * The command label for the teleport request command
     */
    public static final String TP_REQUEST = "tpr";

    /**
     * The command label for the teleport-here request command
     */
    public static final String TP_REQUEST_HERE = "tprhere";

    /**
     * The command label for the teleport-all-here request command
     */
    public static final String TP_REQUEST_HERE_ALL = "tprhereall";

    /**
     * The command label for the teleport-accept command
     */
    public static final String TP_ACCEPT = "tpaccept";

    /**
     * The command label for the teleport-deny command
     */
    public static final String TP_DENY = "tpdeny";

    /**
     * The command label for the teleport-cancel command
     */
    public static final String TP_CANCEL = "tpcancel";

    /**
     * The command label for the teleport-auto-deny command
     */
    public static final String TP_TOGGLE = "tptoggle";

    /**
     * The command label for the teleport-override command
     */
    public static final String TP_OVERRIDE = "tpo";

    /**
     * The command label for the teleport-here-override command
     */
    public static final String TP_OVERRIDE_HERE = "tpohere";

    /**
     * The command label for the teleport-all-here-override command
     */
    public static final String TP_OVERRIDE_HERE_ALL = "tpohereall";

    private TeleportationRegistrar() {
        TpToggle.init();
        registerCommands();
        registerEventListeners();
    }

    public void registerCommands() {
        registerCommand(BACK, new Back());

        registerCommand(TP_REQUEST, new TpRequest());
        registerCommand(TP_REQUEST_HERE, new TpRequestHere());
        registerCommand(TP_REQUEST_HERE_ALL, new TpRequestHereAll());

        registerCommand(TP_ACCEPT, new TpAccept());
        registerCommand(TP_DENY, new TpDeny());
        registerCommand(TP_CANCEL, new TpCancel());

        registerCommand(TP_TOGGLE, TpToggle.getInstance());
    }

    private void registerEventListeners() {
        registerEvents(new BackDeathEventListener());
    }

}
