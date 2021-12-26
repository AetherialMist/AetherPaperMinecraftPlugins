package aetherial.spigot.plugin.annotation.permission;

import java.lang.annotation.*;

/**
 * Defines a child permission under a permission in the plugin.yml
 * <p>
 * ###
 * # permissions:
 * #   PARENT:
 * #     children:
 * #       NAME: INHERIT
 * ###
 * See https://www.spigotmc.org/wiki/plugin-yml/
 */
@SuppressWarnings("unused")
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ChildPermissions.class)
public @interface ChildPermission {

    String parent();
    String name();
    boolean inherit() default true;

}
