package com.github.aetherialmist.aether.essentials.vanilla;

import com.github.aetherialmist.aether.essentials.exception.AlreadyInitialized;
import com.github.aetherialmist.aether.essentials.vanilla.commands.Ban;

import static com.github.aetherialmist.aether.essentials.CommandRegistration.registerCommand;

/**
 * Handles registration for the Vanilla module
 * <p>
 * These are commands are meant to replicate vanilla commands to be able to
 * give non-op players access to them.
 */
public class VanillaRegistrar {

    private static VanillaRegistrar instance;

    /**
     * Initialize this module and all of its dependencies
     */
    public static void init() {
        if (instance != null) {
            throw new AlreadyInitialized(VanillaRegistrar.class);
        }
        instance = new VanillaRegistrar();
    }

    /**
     * The command label for the ban command
     */
    public static final String BAN = "ban";

    public static final String PARDON = "pardon";

    public static final String TP = "tp";
    public static final String KICK = "kick";

    private VanillaRegistrar() {
        registerCommands();
    }

    private void registerCommands() {
        registerCommand(BAN, new Ban());
    }

}
