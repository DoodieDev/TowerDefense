package doodieman.towerdefense.utils;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@UtilityClass
public final class FileUtils {

    public static void createDirsAndFile(final File file) {
        createDirsAndFile(file.toPath());
    }

    public static void createDirsAndFile(final Path path) {
        try {
            createDirs(path);

            if (!Files.exists(path))
                Files.createFile(path);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createDirs(final File file) {
        createDirs(file.toPath());
    }

    public static void createDirs(final Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (! Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }

}
