package aetherial.spigot.plugin.annotation.plugin;

import java.lang.annotation.*;

/**
 * Defines the website for this plugin.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Website {

    String value();

}
