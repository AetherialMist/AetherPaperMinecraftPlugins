package aetherial.spigot.plugin.annotation;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides a way to build a custom ordered YAML file, instead of using SnakeYaml's default order of alphanumeric,
 */
public class PluginYamlBuilder {

    private static final Tag TAG_MAP = Tag.MAP;
    private static final DumperOptions.FlowStyle FLOW_STYLE = DumperOptions.FlowStyle.BLOCK;

    private final Yaml yaml = new Yaml();
    private final StringBuilder raw = new StringBuilder();
    private final Map<String, Object> map;

    /**
     * @param map The Map holding all the data for the YAML file
     */
    public PluginYamlBuilder(Map<String, Object> map) {
        this.map = map;
    }

    /**
     * Append data to the raw YAML file data
     *
     * @param key The key of the data to append
     * @return This builder for repeated calls
     */
    public PluginYamlBuilder append(String key) {
        if (map.containsKey(key)) {
            // Seems wasteful, but after an hour of attempts this was the only successful way.
            Map<String, Object> singleMapping = new HashMap<>();
            singleMapping.put(key, map.get(key));
            raw.append(yaml.dumpAs(singleMapping, TAG_MAP, FLOW_STYLE));
        }
        return this;
    }

    /**
     * Get the String version of the YAML contents
     *
     * @return The String version of the YAML contents
     */
    public String getRawYamlContents() {
        return raw.toString();
    }

}