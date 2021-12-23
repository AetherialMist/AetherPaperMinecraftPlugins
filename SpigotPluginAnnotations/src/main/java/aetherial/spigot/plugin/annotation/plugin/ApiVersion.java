package aetherial.spigot.plugin.annotation.plugin;

import java.lang.annotation.*;

/**
 * The Minecraft version the plugin targets.
 * <p>
 * Just the major version: 1.13, 1.14, etc.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ApiVersion {

    String value();

}
