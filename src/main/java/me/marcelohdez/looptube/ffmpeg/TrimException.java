package me.marcelohdez.looptube.ffmpeg;

public class TrimException extends Exception {
    public TrimException(String message) {
        super(message);
    }

    /** Constructs a TrimException with a vague message indicating SOME error occurred */
    public TrimException() {
        this("Could not trim your audio! Maybe your start and times are outside of the song?");
    }
}
