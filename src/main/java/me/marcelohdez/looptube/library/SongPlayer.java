package me.marcelohdez.looptube.library;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import me.marcelohdez.looptube.dialog.ErrorDialog;

import javax.swing.*;
import java.io.*;
import java.util.Optional;

/**
 * An ergonomic wrapper over jLayer's AdvancedPlayer, with support for
 * stopping and resuming. Plays music on a worker thread to not stop GUI.
 */
public class SongPlayer extends PlaybackListener {
    private SwingWorker<Boolean, Integer> worker;

    private File source;
    private AdvancedPlayer player;
    /** Milliseconds per frame, set when source is changed */
    private double framerate = 0.0f;
    /** Current/last stopped at position in MPEG frames */
    private int framePos = 0;
    /** System milliseconds when song is started, should only change in PlaybackListener events */
    private long startMS = -1;
    /** Whether the player should attempt to loop when it finishes the song */
    private boolean repeating = false;

    public boolean isPlaying() {
        return worker != null && !worker.isDone();
    }

    public Optional<File> getSource() {
        return Optional.ofNullable(source);
    }

    public void setRepeating(boolean b) {
        repeating = b;
    }

    /** Changes this SongPlayer's source file, resetting the current position */
    public void setSource(File f) throws JavaLayerException, IOException {
        source = f;
        stop();
        framePos = 0;

        var bs = new Bitstream(new FileInputStream(source));
        var header = bs.readFrame();
        if (header == null) throw new IOException("File does not contain frame data!");
        framerate = header.ms_per_frame();
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
                // player already stops upon ending, so we only do it if cancelled: stops a NullPointerException
                if (isCancelled()) { // forcibly stopped
                    player.stop();
                } else if (repeating) {
                    try {
                        restart();
                    } catch (JavaLayerException | IOException e) {
                        new ErrorDialog(null, "Could not replay song! Is the file missing?");
                    }
                }
            }
        };

        worker.execute();
    }

    public void stop() {
        if (worker != null) {
            worker.cancel(true);
            worker = null;
        }
    }

    private void restart() throws IOException, JavaLayerException {
        stop();
        framePos = 0;
        start();
    }

    /**
     * Will reset song if it is more than 3 seconds from start, and return false.
     * Otherwise, it will only return true (but will continue playing if caller does not
     * stop it).
     *
     * @return false if song was reset, true if caller should move to previous track.
     */
    public boolean previous() throws IOException, JavaLayerException {
        if (System.currentTimeMillis() - startMS > 3000) {
            restart();
            return false;
        }

        return true;
    }

    private void createPlayerFromSource() throws JavaLayerException, IOException {
        if (source == null) return;
        player = new AdvancedPlayer(new FileInputStream(source));
        player.setPlayBackListener(this);
    }

    @Override
    public void playbackStarted(PlaybackEvent evt) {
        startMS = System.currentTimeMillis();
    }

    @Override
    public void playbackFinished(PlaybackEvent evt) {
        startMS = -1; // no longer playing
        // after some digging, found out evt.getFrame actually returns milliseconds... here we fix it:
        // we add to get the difference since last unpause
        framePos += (int) ((evt.getFrame() / framerate) + 0.5); // + 0.5 rounds to nearest int
    }
}
