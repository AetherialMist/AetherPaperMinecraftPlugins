package aetherial.spigot.plugin.annotation.plugin;

import java.lang.annotation.*;

/**
 * A list of plugins that should load AFTER this plugin.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LoadBefore {

    String[] value() default {};

}
