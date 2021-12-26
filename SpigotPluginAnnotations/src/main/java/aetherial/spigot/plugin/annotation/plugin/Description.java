package aetherial.spigot.plugin.annotation.plugin;

import java.lang.annotation.*;

/**
 * The description of the plugin
 * <p>
 * If used, MUST appear on same Element as {@link Plugin}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Description {

    String value() default "";

}
