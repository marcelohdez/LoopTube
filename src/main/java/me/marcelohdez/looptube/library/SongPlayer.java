package me.marcelohdez.looptube.library;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import javax.swing.*;
import java.io.*;

/**
 * An ergonomic wrapper over jLayer's AdvancedPlayer, with support for
 * stopping and resuming. Plays music on a worker thread to not stop GUI.
 */
public class SongPlayer {
    private SwingWorker<Boolean, Integer> worker;

    private File source;
    private AdvancedPlayer player;
    /** Milliseconds per frame, set when source is changed */
    private double framerate = 0.0f;
    /** Current/last stopped at position in MPEG frames */
    private int framePos = 0;
    /** Whether we ignore the next stop position (used when starting new song) */
    private boolean ignorePos = false;

    public boolean isPlaying() {
        return worker != null && !worker.isDone();
    }

    /** Changes this SongPlayer's source file, resetting the current position */
    public void setSource(File f) throws JavaLayerException, IOException {
        source = f;
        framePos = 0;
        ignorePos = true;

        var bs = new Bitstream(new FileInputStream(source));
        framerate = bs.readFrame().ms_per_frame();
        stop();
    }

    /**
     * Will start playing music from current source file on a worker thread
     * starting at the current position (will be 0 if setSource was called
     * before this method).
     *
     * <p>Will do nothing if currently playing or source has not been set.</p>
     */
    public void start() throws JavaLayerException, IOException {
        if (isPlaying() || source == null) return;
        createPlayerFromSource();

        worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws JavaLayerException {
                if (!isCancelled()) return player.play(framePos, Integer.MAX_VALUE);

                return false;
            }
            @Override
            protected void done() {
                player.stop();
            }
        };

        worker.execute();
    }

    public void stop() {
        if (worker != null) {
            worker.cancel(true);
            worker = null;
            ignorePos = false;
        }
    }

    private void createPlayerFromSource() throws JavaLayerException, IOException {
        if (source == null) return;
        player = new AdvancedPlayer(new FileInputStream(source));
        player.setPlayBackListener(consumePlaybackEvents());
    }

    private PlaybackListener consumePlaybackEvents() {
        return new PlaybackListener() {
            @Override
            public void playbackFinished(PlaybackEvent evt) {
                // after some digging, found out evt.getFrame actually returns milliseconds... here we fix it:
                // we add to get the difference since last unpause
                if (!ignorePos) framePos += (int) ((evt.getFrame() / framerate) + 0.5); // + 0.5 rounds to nearest int
            }
        };
    }
}
