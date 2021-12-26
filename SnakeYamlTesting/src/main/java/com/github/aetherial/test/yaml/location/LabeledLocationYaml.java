package com.github.aetherial.test.yaml.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LabeledLocationYaml {

    private String label;
    private LocationYaml locationYaml;

    public LabeledLocationYaml(String label, LocationYaml locationYaml) {
        this.label = label;
        this.locationYaml = locationYaml;
    }

    public String toString() {
        return String.format("{%s: %s}", label, locationYaml);
    }
}
