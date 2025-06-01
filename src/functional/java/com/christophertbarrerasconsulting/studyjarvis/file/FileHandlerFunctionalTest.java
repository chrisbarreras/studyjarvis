package com.christophertbarrerasconsulting.studyjarvis.file;

import org.apache.poi.sl.draw.geom.PathCommand;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileHandlerFunctionalTest {
    @Test
    public void createAndDeleteFolder () throws IOException {
        String folder = FileHandler.createNewTempFolder("foo");
        assertTrue(FileHandler.directoryExists(folder));

        String file = FileHandler.concatenatePath(folder, "file");
        FileHandler.writeTextToFile("foo", file);
        assertTrue(Files.exists(Path.of(file)));

        FileHandler.deletePathIfExists(Path.of(folder));
        assertFalse(FileHandler.directoryExists(folder));
    }

    @Test
    public void createAndDeleteFile () throws IOException {
        String folder = FileHandler.createNewTempFolder("foo");
        assertTrue(FileHandler.directoryExists(folder));

        assertDoesNotThrow(() -> FileHandler.deletePathIfExists(FileHandler.concatenatePath(folder, "NoExist")));

        String file = FileHandler.concatenatePath(folder, "file");
        FileHandler.writeTextToFile("foo", file);
        assertTrue(Files.exists(Path.of(file)));

        FileHandler.deletePathIfExists(file);
        assertFalse(Files.exists(Path.of(file)));

        FileHandler.deletePathIfExists(Path.of(folder));
        assertFalse(FileHandler.directoryExists(folder));
    }
}
