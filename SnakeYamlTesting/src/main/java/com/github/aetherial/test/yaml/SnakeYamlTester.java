package com.github.aetherial.test.yaml;

import com.github.aetherial.test.yaml.location.LabeledLocationYaml;
import com.github.aetherial.test.yaml.location.LocationYaml;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.*;


@Slf4j
public class SnakeYamlTester {

    public static void main(String[] args) {
        SnakeYamlTester tester = new SnakeYamlTester();
        tester.run();
    }

    private SnakeYamlTester() {

    }

    public void run() {
        LocationYaml locationYaml = new LocationYaml("1234-3465-4567", 0.123, 4.1235, 0, 90.001f, 14.6246f);
        LabeledLocationYaml labeledLocationYaml = new LabeledLocationYaml("pine", locationYaml);

        Yaml yaml = new Yaml();
        String data = yaml.dumpAs(labeledLocationYaml, Tag.MAP, DumperOptions.FlowStyle.BLOCK);

        File file = new File("test.yml");
        try (Writer writer = new FileWriter(file)) {
            writer.write(data);
            writer.flush();
        } catch (IOException e) {
            log.warn("Failed to write file data");
        }

        yaml = new Yaml(new Constructor(LabeledLocationYaml.class));
        try (Reader reader = new FileReader(file)) {
            LabeledLocationYaml testFromFile = yaml.loadAs(reader, LabeledLocationYaml.class);
            log.info(testFromFile.toString());
        } catch (IOException e) {
            log.warn("Failed to read file data");
        }

        log.info(Outside.read(file, LabeledLocationYaml.class).toString());

        LabeledLocationYaml testRecord = yaml.loadAs(data, LabeledLocationYaml.class);
        log.info(data);
        log.info(labeledLocationYaml.toString());
        log.info(testRecord.toString());
    }

}
