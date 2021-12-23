package aetherial.spigot.plugin.annotation.plugin;

import java.lang.annotation.*;

/**
 * The description of the plugin
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Description {

    String value() default "";

}
