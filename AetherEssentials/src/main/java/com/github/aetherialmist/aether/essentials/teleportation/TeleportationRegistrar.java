package com.github.aetherialmist.aether.essentials.teleportation;

import com.github.aetherialmist.aether.essentials.exception.AlreadyInitialized;
import com.github.aetherialmist.aether.essentials.teleportation.command.*;
import com.github.aetherialmist.aether.essentials.teleportation.eventlistener.BackDeathEventListener;

import static com.github.aetherialmist.aether.essentials.CommandRegistration.registerCommand;
import static com.github.aetherialmist.aether.essentials.EventRegistration.registerEvents;

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
