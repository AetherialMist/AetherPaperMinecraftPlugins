package aetherial.spigot.plugin.annotation.plugin;

import java.lang.annotation.*;

/**
 * A list of plugins that are required for this plugin to have full functionality.
 * <p>
 * This plugin will load after any listed here.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SoftDepend {

    String[] value() default {};

}
