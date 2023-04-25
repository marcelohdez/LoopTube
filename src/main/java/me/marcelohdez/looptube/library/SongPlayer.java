package me.marcelohdez.looptube.library;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import javax.swing.*;
import java.io.*;

public class SongPlayer {
    private SwingWorker<Boolean, Integer> worker;
    private AdvancedPlayer player;
    private boolean playing = false;
    private int framePos = 0;

    public boolean isPlaying() {
        return playing;
    }

    public void setSource(File f) throws JavaLayerException, IOException {
        stop();

        player = new AdvancedPlayer(new FileInputStream(f));
        player.setPlayBackListener(consumePlaybackEvents());
    }

    public void start() {
        if (worker == null) {
            worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() throws JavaLayerException {
                    var res = false;
                    if (!isCancelled()) {
                        res = player.play(framePos, Integer.MAX_VALUE);
                        framePos = 0;
                    }

                    return res;
                }
                @Override
                protected void done() {
                    player.stop();
                }
            };
        }

        if (worker.getState() == SwingWorker.StateValue.PENDING) worker.execute();
    }

    public void stop() {
        if (worker != null) {
            worker.cancel(true);
            worker = null;
        }
    }

    private PlaybackListener consumePlaybackEvents() {
        return new PlaybackListener() {
            @Override
            public void playbackStarted(PlaybackEvent evt) {
                playing = true;
            }

            @Override
            public void playbackFinished(PlaybackEvent evt) {
                playing = false;
                framePos = evt.getFrame();
            }
        };
    }
}
