package me.marcelohdez.looptube.ffmpeg;

import me.marcelohdez.looptube.AppController;
import me.marcelohdez.looptube.library.SongData;

import java.io.*;

public final class FFMPEGWrapper {
    private FFMPEGWrapper() {}

    public static int attemptAndWait(
            SongData song,
            Duration start,
            Duration end
    ) throws IOException, InterruptedException, TrimException {
        var duration = end.minus(start); // ffmpeg uses start + duration rather than start + end
        var newName = song + " (trimmed %s-%s)".formatted(start.toString(), end.toString()) + SongData.EXTENSION;

        var process = createProcess(song, start, duration, newName);
        process.waitFor();

        try (var br = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            // remove empty file created by ffmpeg
            var parentDir = song.getFile().getParent();
            if (parentDir != null) {
                var createdFile = new File(parentDir + File.separator + newName);
                if (createdFile.length() <= 256L) {
                    if (!createdFile.delete()) System.err.println("Could not delete " + createdFile);
                } else return process.exitValue(); // assume success. (ffmpeg always returns 0??)
            }

            // for some reason ffmpeg ONLY uses error stream so everything including error is here.
            System.err.println("\nFFMPEG error/output:");
            var err = br.readLine();
            while (err != null) {
                System.err.println(err);
                err = br.readLine();
            }
        }
        throw new TrimException();
    }

    private static Process createProcess(
            SongData song,
            Duration start,
            Duration duration,
            String out
    ) throws IOException {
        var pb = new ProcessBuilder(
                "ffmpeg",
                "-ss", start.toString(),
                "-i", song.getFile().getName(),
                "-t", duration.toString(),
                out
        );

        pb.directory(new File(AppController.LIBRARY_DIR));
        return pb.start();
    }
}
