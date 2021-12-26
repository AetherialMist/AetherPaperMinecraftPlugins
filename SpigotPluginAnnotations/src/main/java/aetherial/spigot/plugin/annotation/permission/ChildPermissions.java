package aetherial.spigot.plugin.annotation.permission;


import java.lang.annotation.*;

/**
 * Container for repeatable {@link ChildPermission}.
 * <p>
 * Should NOT be used directly.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChildPermissions {

    ChildPermission[] value() default {};

}
