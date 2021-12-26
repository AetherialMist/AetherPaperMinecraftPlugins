package aetherial.spigot.plugin.annotation.plugin;

import java.lang.annotation.*;

/**
 * Defines the author of the plugin.
 * <p>
 * Mutually exclusive with {@link Authors}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Author {

    String value();

}
