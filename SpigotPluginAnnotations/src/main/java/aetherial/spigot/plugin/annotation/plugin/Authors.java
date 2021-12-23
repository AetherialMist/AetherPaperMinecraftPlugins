package aetherial.spigot.plugin.annotation.plugin;

import java.lang.annotation.*;

/**
 * Defines multiple authors of the plugin.
 * <p>
 * Mutually exclusive with {@link Author}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Authors {

    String[] value() default {};

}
