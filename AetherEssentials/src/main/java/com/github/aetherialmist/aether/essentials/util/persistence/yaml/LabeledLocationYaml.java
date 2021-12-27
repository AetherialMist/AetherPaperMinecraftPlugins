package com.github.aetherialmist.aether.essentials.util.persistence.yaml;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Server;

@Getter
@Setter
@NoArgsConstructor
public class LabeledLocationYaml {

    private String label;
    private LocationYaml locationYaml;

    public LabeledLocationYaml(String label, Location location) {
        this.label = label;
        this.locationYaml = new LocationYaml(location);
    }

    public LabeledLocationYaml(String label, LocationYaml locationYaml) {
        this.label = label;
        this.locationYaml = locationYaml;
    }

    public Location asLocation(Server server) {
        return locationYaml.asLocation(server);
    }

    public String toString() {
        return String.format("{%s: %s}", label, locationYaml);
    }
}
