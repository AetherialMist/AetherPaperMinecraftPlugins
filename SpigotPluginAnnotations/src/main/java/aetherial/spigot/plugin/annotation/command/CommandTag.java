package aetherial.spigot.plugin.annotation.command;

import java.lang.annotation.*;

/**
 * This annotation goes on a Class, Interface or Enum.
 * Available to Java compiler, but not the JVM.
 * Included in the documentation.
 * <p>
 * In the plugin.yml
 * ###
 * # commands:
 * #   NAME:
 * #     desc: DESCRIPTION
 * #     aliases: ALIASES
 * #     permission: PERMISSION
 * #     permissionMessage: MESSAGE
 * #     usage: USAGE
 * ###
 * See https://www.spigotmc.org/wiki/plugin-yml/
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CommandsTag.class)
public @interface CommandTag {

    String name();
    String desc();
    String usage();
    String permission();

    String[] aliases() default {};
    String permissionMessage() default "";

}
