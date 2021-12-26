package aetherial.spigot.plugin.annotation.command;

import java.lang.annotation.*;

/**
 * Container for repeatable {@link CommandTag}.
 * <p>
 * Should NOT be used directly.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandsTag {

    CommandTag[] value() default {};

}
