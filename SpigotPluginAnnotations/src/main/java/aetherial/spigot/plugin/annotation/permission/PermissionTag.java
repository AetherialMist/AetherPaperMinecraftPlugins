package aetherial.spigot.plugin.annotation.permission;

import org.bukkit.permissions.PermissionDefault;

import java.lang.annotation.*;

/**
 * Defines a permission in the plugin.yml
 * <p>
 * ###
 * # permissions:
 * #   NAME:
 * #     description: DESC
 * #     default: DEFAULT_VALUE
 * #     CHILDREN:
 * #       NAME: INHERIT
 * #       NAME: INHERIT
 * #       NAME: INHERIT
 * ###
 * See https://www.spigotmc.org/wiki/plugin-yml/
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(PermissionsTag.class)
public @interface PermissionTag {

    String name();
    String desc();
    PermissionDefault defaultValue() default PermissionDefault.OP;

}
