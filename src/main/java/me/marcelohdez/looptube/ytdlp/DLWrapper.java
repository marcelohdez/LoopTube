package me.marcelohdez.looptube.ytdlp;

import me.marcelohdez.looptube.AppController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DLWrapper {
    private static final String OUTPUT_DIR = AppController.LIBRARY_DIR + "%(title)s";
    private final String url;
    private Process p;

    public DLWrapper(String url) {
        this.url = url;
    }

    public int startAndWait() throws IOException, InterruptedException {
        p = new ProcessBuilder(
                "yt-dlp",
                "-o", OUTPUT_DIR, // set output
                "-x", // extract audio
                "--audio-format", "mp3", // convert to mp3 with ffmpeg
                url // url parameter
        ).start();

        return p.waitFor();
    }

    /** Will throw a DLException if there was an error running yt-dlp */
    public void verifyOutput() throws IOException, DLException {
        try (var br = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
            var err = br.readLine();
            if (err == null) return; // no error!

            err = err.toLowerCase();
            // check for specific known error type
            if (err.contains("is not a valid url")) {
                throw new DLException("The URL given did not work! (Make sure it is a video!)");
            } else if (err.contains("urlopen error")) {
                throw new DLException("Could not connect to URL! (Check your network?)");
            }

            // print said error to console
            String line = "\nYT-DLP Error:";
            while (line != null) {
                System.out.println(line);
                line = br.readLine();
            }

            throw new DLException();
        }
    }
}
