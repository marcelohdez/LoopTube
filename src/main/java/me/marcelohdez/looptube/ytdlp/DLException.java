package me.marcelohdez.looptube.ytdlp;

public class DLException extends Exception {
    public DLException(String message) {
        super(message);
    }

    /** Constructs a DLException with a vague message indicating SOME error occurred */
    public DLException() {
        this("An error occurred getting your audio!");
    }
}
