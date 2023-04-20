package me.marcelohdez.looptube.library;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SongPlayer {
    private AdvancedPlayer player;
    private int framePos = 0;
    private boolean playing = false;

    public boolean isPlaying() {
        return playing;
    }

    public void setSource(File f) throws JavaLayerException, FileNotFoundException {
        if (player != null) player.close();

        var fis = new FileInputStream(f);
        player = new AdvancedPlayer(fis);
        player.setPlayBackListener(consumePlaybackEvents());
    }

    public void start() throws JavaLayerException, FileNotFoundException {
        //setSource(f); // restart file input stream
        player.play(framePos, Integer.MAX_VALUE);
    }

    public void stop() {
        player.stop();
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
