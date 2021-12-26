package com.github.aetherial.test.yaml;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Outside {

    private Outside() {

    }

    public static <T> T read(File file, Class<T> type) {
        T data = null;
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Yaml yaml = new Yaml(new Constructor(type));
                data = yaml.loadAs(reader, type);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
