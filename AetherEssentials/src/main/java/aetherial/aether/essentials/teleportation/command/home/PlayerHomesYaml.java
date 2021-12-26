package aetherial.aether.essentials.teleportation.command.home;

import aetherial.aether.essentials.util.persistence.yaml.LabeledLocationYaml;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class PlayerHomesYaml {

    private String playerUUID;
    private LabeledLocationYaml[] homes;

    public PlayerHomesYaml(String playerUUID, LabeledLocationYaml[] homes) {
        this.playerUUID = playerUUID;
        this.homes = homes;
    }

    public PlayerHomesYaml(String playerUUID, Map<String, Location> homes) {
        this.playerUUID = playerUUID;

        List<LabeledLocationYaml> temp = new ArrayList<>();
        for (Map.Entry<String, Location> home : homes.entrySet()) {
            temp.add(new LabeledLocationYaml(home.getKey(), home.getValue()));
        }

        this.homes = temp.toArray(new LabeledLocationYaml[homes.size()]);
    }

}
