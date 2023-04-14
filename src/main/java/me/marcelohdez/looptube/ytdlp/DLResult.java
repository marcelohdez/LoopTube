package me.marcelohdez.looptube.ytdlp;

public enum DLResult {
    InvalidURL("The URL given did not work! (Make sure it is a video!)"),
    URLOpenFail("Could not connect to URL! (Check your network?)"),
    CannotRunYTDLP("Could not open yt-dlp! (Is it installed?)"),
    Error("An error occurred getting your audio!"),
    Success("No errors!");

    public String message() {
        return msg;
    }

    DLResult(String msg) {
        this.msg = msg;
    }

    private final String msg;
}
