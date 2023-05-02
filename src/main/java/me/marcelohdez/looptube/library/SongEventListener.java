package me.marcelohdez.looptube.library;

public interface SongEventListener {
    void songFinished();
    /** The song started playing, whether from start or unpaused */
    void songIsPlaying();
    /** Song was stopped, before finishing */
    void songStopped();
}
