package aetherial.spigot.plugin.annotation.plugin;

import java.lang.annotation.*;

/**
 * Defines the main class of the plugin.
 * <p>
 * MUST be used on the main class that extends {@link org.bukkit.plugin.java.JavaPlugin}.
 * This is the only required annotation for a plugin.
 * <p>
 * plugin.yml
 * ###
 * # main: CLASS_THIS_IS_ON
 * # name: NAME
 * # version: VERSION
 * ###
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Plugin {

    String name();

    String version();

}
