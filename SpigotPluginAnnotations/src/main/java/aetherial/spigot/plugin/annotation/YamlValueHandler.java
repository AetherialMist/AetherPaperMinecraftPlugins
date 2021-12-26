package aetherial.spigot.plugin.annotation;

import java.lang.annotation.Annotation;

/**
 * Provides a lambda way of getting an Annotation's data for the plugin.yml file
 *
 * @param <A> The Annotation type
 */
public interface YamlValueHandler<A extends Annotation> {

    /**
     * Get the data for the plugin.yml file
     *
     * @param annotation The Annotation type
     * @return The data for the plugin.yml file
     */
    Object getYamlValue(A annotation);

}
