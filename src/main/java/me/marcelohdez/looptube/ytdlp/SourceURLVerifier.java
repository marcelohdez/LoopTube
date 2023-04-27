package me.marcelohdez.looptube.ytdlp;

import javax.swing.*;
import java.util.Optional;

/** Very simple YouTube URL verifier */
public class SourceURLVerifier extends InputVerifier {
    private static final String PLAYLIST_IDENTIFIER = "&list=";
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

    /** If this link represents a playlist, it will return the video section, otherwise empty */
    public Optional<String> getVideoURLOnly(JComponent input) {
        if (!(input instanceof JTextField jf))
            throw new RuntimeException("SourceURLVerifier used with a non-JTextField!");

        var text = jf.getText();
        var io = text.indexOf(PLAYLIST_IDENTIFIER);
        if (io != -1) {
            return Optional.of(text.substring(0, io));
        } else return Optional.empty();
    }
}
