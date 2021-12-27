package com.github.aetherialmist.aether.essentials.util.persistence;

import com.github.aetherialmist.aether.essentials.util.persistence.yaml.LabeledLocationYaml;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class PersistenceTest {

    private final static String testFolderName = "testFolder";
    private static File testFolder;

    private Persistence persistence;

    @BeforeAll
    public static void folderSetup() {
        testFolder = new File(testFolderName);
        boolean folderCreated = testFolder.mkdirs();
        if (!folderCreated) {
            System.err.println("Failed to create folder for test files");
        }
    }

    @AfterAll
    public static void folderTeardown() {
        try {
            Optional<File[]> optionalFiles = Optional.ofNullable(testFolder.listFiles());
            if (optionalFiles.isEmpty()) {
                System.err.println("Failed to cleanup files");
                return;
            }
            File[] files = optionalFiles.get();

            for (File file : files) {
                Files.delete(file.toPath());
            }
            Files.delete(testFolder.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void setup() {
        JavaPlugin mockPlugin = mock(JavaPlugin.class);
        when(mockPlugin.getDataFolder()).thenReturn(testFolder);

        Persistence.init(mockPlugin);
        persistence = Persistence.getInstance();
    }

    @Test
    void writeFileYaml() {
        String name = "pine";
        Location location = getMockedLocation("1234-2356", 0, 0, 0, 0, 0);
        LabeledLocationYaml labeledLocationYaml = new LabeledLocationYaml(name, location);

        persistence.writeFileYaml(name, labeledLocationYaml);

        Optional<LabeledLocationYaml> optional = persistence.readYamlFile(name, LabeledLocationYaml.class);

        Assertions.assertTrue(optional.isPresent());
        Assertions.assertNotNull(optional.get());
        Assertions.assertEquals(labeledLocationYaml.toString(), optional.get().toString());
    }

    @SuppressWarnings("SameParameterValue")
    private Location getMockedLocation(String worldUid, double x, double y, double z, float yaw, float pitch) {
        UUID uuid = mock(UUID.class);
        when(uuid.toString()).thenReturn(worldUid);

        World world = mock(World.class);
        when(world.getUID()).thenReturn(uuid);

        Location location = mock(Location.class);
        when(location.getWorld()).thenReturn(world);
        when(location.getX()).thenReturn(x);
        when(location.getY()).thenReturn(y);
        when(location.getZ()).thenReturn(z);
        when(location.getYaw()).thenReturn(yaw);
        when(location.getPitch()).thenReturn(pitch);

        return location;
    }

}