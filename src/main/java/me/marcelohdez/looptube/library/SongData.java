package me.marcelohdez.looptube.library;

import java.io.File;
import java.util.Optional;

public class SongData {
    private static final String EXTENSION = ".mp3";

    private final File file;
    private String name;

    private SongData(File file) {
        this.file = file;
        name = getMP3Title(file.getName());
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean setName(String newName) {
        var parent = file.getParent();
        var res = parent != null && file.renameTo(new File(parent + File.separator + newName + EXTENSION));

        if (res) name = newName;

        return res;
    }

    public static Optional<SongData> from(File f) {
        if (!f.getName().endsWith(EXTENSION)) return Optional.empty();

        return Optional.of(new SongData(f));
    }

    private static String getMP3Title(String filename) {
        return filename.substring(0, filename.length() - EXTENSION.length());
    }
}
