package com.github.aetherial.test.yaml;

import com.github.aetherial.test.yaml.location.LabeledLocationYaml;
import com.github.aetherial.test.yaml.location.LocationYaml;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.*;


public class SnakeYamlTester {

    public static void main(String[] args) throws IOException {
        new SnakeYamlTester();
    }

    public SnakeYamlTester() throws RuntimeException, IOException {
        LocationYaml locationYaml = new LocationYaml("1234-3465-4567", 0.123, 4.1235, 0, 90.001f, 14.6246f);
        LabeledLocationYaml record = new LabeledLocationYaml("pine", locationYaml);

        File file = new File("test.yml");
        Writer writer = new FileWriter(file);

        Yaml yaml = new Yaml();
        String data = yaml.dumpAs(record, Tag.MAP, DumperOptions.FlowStyle.BLOCK);
        writer.write(data);
        writer.flush();
        writer.close();

        Reader reader = new FileReader(file);
        yaml = new Yaml(new Constructor(LabeledLocationYaml.class));
        LabeledLocationYaml testFromFile = yaml.loadAs(reader, LabeledLocationYaml.class);
        System.out.println(testFromFile);

        System.out.println(Outside.read(file, LabeledLocationYaml.class));

        LabeledLocationYaml testRecord = yaml.loadAs(data, LabeledLocationYaml.class);
        System.out.println(data);
        System.out.println(record);
        System.out.println(testRecord);
    }

}
