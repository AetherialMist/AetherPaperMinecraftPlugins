package com.github.aetherialmist.aether.essentials.vanilla;

import com.github.aetherialmist.aether.essentials.exception.AlreadyInitialized;
import com.github.aetherialmist.aether.essentials.vanilla.commands.Ban;

import static com.github.aetherialmist.aether.essentials.CommandRegistration.registerCommand;

public class VanillaRegistrar {

    private static VanillaRegistrar instance;

    public static void init() {
        if (instance != null) {
            throw new AlreadyInitialized(VanillaRegistrar.class);
        }
        instance = new VanillaRegistrar();
    }

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
