package me.marcelohdez.looptube.library;

import java.io.File;
import java.util.Optional;

public class SongData {
    public static final String EXTENSION = ".mp3";

    private File file;
    private String name;

    private SongData(File file) {
        this.file = file;
        var filename = file.getName();
        name = filename.substring(0, filename.length() - EXTENSION.length());
    }

    public static Optional<SongData> from(File f) {
        if (!f.getName().endsWith(EXTENSION)) return Optional.empty();

        return Optional.of(new SongData(f));
    }

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean setName(String newName) {
        var parent = file.getParent();
        var newFile = new File(parent + File.separator + newName + EXTENSION);
        var res = parent != null && file.renameTo(newFile);

        if (res) {
            name = newName;
            file = newFile;
        }

        return res;
    }
}
