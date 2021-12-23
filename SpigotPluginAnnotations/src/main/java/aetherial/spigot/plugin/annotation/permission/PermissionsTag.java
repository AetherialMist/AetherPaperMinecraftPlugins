package aetherial.spigot.plugin.annotation.permission;

import java.lang.annotation.*;

/**
 * Container for repeatable {@link PermissionTag}.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionsTag {

    PermissionTag[] value() default {};

}
