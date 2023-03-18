package me.marcelohdez.looptube.ytdlp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DLWrapper {
    private final String url;
    private Process p;

    public DLWrapper(String url) {
        this.url = url;
    }

    public int startAndWait() throws IOException, InterruptedException {
        p = new ProcessBuilder(
                "yt-dlp",
                "-x",
                "--audio-format",
                "mp3",
                url
        ).start();

        return p.waitFor();
    }

    public DLResult getResult() throws IOException {
        try (var br = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
            var err = br.readLine();
            if (err != null) {
                if (err.toLowerCase().contains("is not a valid url")) {
                    return DLResult.InvalidURL;
                } else return DLResult.Error;
            }
        }

        return DLResult.Success;
    }
}
