package com.github.aetherial.test.yaml;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

@Slf4j
public class Outside {

    private Outside() {

    }

    public static <T> T read(File file, Class<T> type) {
        log.info("TESTING");
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
