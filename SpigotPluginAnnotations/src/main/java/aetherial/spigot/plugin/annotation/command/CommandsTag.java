package aetherial.spigot.plugin.annotation.command;

import java.lang.annotation.*;

/**
 * Container for repeatable {@link CommandTag}.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandsTag {

    CommandTag[] value() default {};

}
