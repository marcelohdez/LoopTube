package me.marcelohdez.looptube.ytdlp;

import javax.swing.*;

/** Very simple YouTube URL verifier */
public class SourceURLVerifier extends InputVerifier {
    private static final String HTTPS = "https://";
    private static final String[] URL_BEGINNINGS = {
            HTTPS + "youtu.be/",
            HTTPS + "youtube.com/watch?v=",
            HTTPS + "www.youtube.com/watch?v="
    };

    @Override
    public boolean verify(JComponent input) {
        if (!(input instanceof JTextField jf))
            throw new RuntimeException("SourceURLVerifier used with a non-JTextField!");

        var text = jf.getText();
        for (var beginning : URL_BEGINNINGS) {
            if (text.startsWith(beginning)) return true;
        }

        return false;
    }
}
