package aetherial.spigot.plugin.annotation.plugin;

import java.lang.annotation.*;

/**
 * Defines when the plugin should load: On Startup or Post world creation.
 * <p>
 * If used, MUST appear on same Element as {@link Plugin}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Load {

    LoadType value() default LoadType.POST_WORLD;

    @SuppressWarnings("unused")
    enum LoadType {
        DEFAULT(null),
        STARTUP("STARTUP"),
        POST_WORLD("POSTWORLD");

        private final String loadTypeValue;

        LoadType(String version) {
            this.loadTypeValue = version;
        }

        public String getLoadType() {
            return loadTypeValue;
        }
    }
}
