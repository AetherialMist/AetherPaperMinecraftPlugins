package aetherial.spigot.plugin.annotation.plugin;

import java.lang.annotation.*;

/**
 * The name to use when logging to console instead of the plugin's name:
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Prefix {

    String value();

}
