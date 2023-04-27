package me.marcelohdez.looptube.dialog;

import me.marcelohdez.looptube.ffmpeg.Duration;
import me.marcelohdez.looptube.ffmpeg.FFMPEGWrapper;
import me.marcelohdez.looptube.ffmpeg.TrimException;
import me.marcelohdez.looptube.library.SongData;

import java.awt.*;
import java.io.IOException;

public class FFMPEGDialog extends LoopTubeDialog {
    private TrimException exception;
    private boolean result = true;

    public FFMPEGDialog(Component summoner) {
        super(summoner, "Trim in progress", "Your song is being trimmed...");
        pack();
    }

    public boolean attempt(SongData song, Duration start, Duration end) throws TrimException {
        setLocationRelativeTo(summoner);

        new Thread(() -> {
            try {
                var res = FFMPEGWrapper.attemptAndWait(song, start, end);
                if (res != 0) result = false;
            } catch (Exception e) {
                if (e instanceof IOException ioe && ioe.getMessage().startsWith("Cannot run program")) {
                    exception = new TrimException("Could not run ffmpeg! Do you have it installed?");
                } else {
                    e.printStackTrace();
                    exception = new TrimException();
                }
            }
            dispose();
        }).start();

        setVisible(true);
        if (exception != null) throw exception;
        return result;
    }
}
