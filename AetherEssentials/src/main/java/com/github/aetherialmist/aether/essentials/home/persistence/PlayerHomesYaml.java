package com.github.aetherialmist.aether.essentials.home.persistence;

import com.github.aetherialmist.aether.essentials.util.persistence.yaml.LabeledLocationYaml;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents the YAML data of a Player's stored homes
 */
@Getter
@Setter
@NoArgsConstructor
public class PlayerHomesYaml {

    private String playerUUID;
    private LabeledLocationYaml[] homes;

    /**
     * @param playerUUID The String version of a Player's UUID
     * @param homes      The YAML representations of the home locations
     */
    @SuppressWarnings("unused")
    public PlayerHomesYaml(String playerUUID, LabeledLocationYaml[] homes) {
        this.playerUUID = playerUUID;
        this.homes = homes;
    }

    /**
     * @param playerUUID The String version of a Player's UUID
     * @param homes      A Map of the Player's home locations
     */
    public PlayerHomesYaml(String playerUUID, Map<String, Location> homes) {
        this.playerUUID = playerUUID;

        List<LabeledLocationYaml> temp = new ArrayList<>();
        for (Map.Entry<String, Location> home : homes.entrySet()) {
            temp.add(new LabeledLocationYaml(home.getKey(), home.getValue()));
        }

        this.homes = temp.toArray(new LabeledLocationYaml[homes.size()]);
    }

}
