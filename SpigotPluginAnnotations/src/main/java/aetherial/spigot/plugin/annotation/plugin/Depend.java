package aetherial.spigot.plugin.annotation.plugin;

import java.lang.annotation.*;

/**
 * A list of plugins that this plugin requires in order to load.
 * <p>
 * The name of the plugin found in their plugin.yml
 * <p>
 * If used, MUST appear on same Element as {@link Plugin}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Depend {

    String[] value() default {};

}
