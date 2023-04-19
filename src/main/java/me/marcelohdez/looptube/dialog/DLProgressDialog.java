package me.marcelohdez.looptube.dialog;

import me.marcelohdez.looptube.ytdlp.DLException;
import me.marcelohdez.looptube.ytdlp.DLWrapper;

import java.awt.*;
import java.io.IOException;

public class DLProgressDialog extends LoopTubeDialog {
    private final Component summoner;
    private DLException exception;

    public DLProgressDialog(Component summoner) {
        super("Progress", "A download is in progress...");
        this.summoner = summoner;
        pack();
    }

    public void attempt(final String url) throws IOException, DLException {
        setLocationRelativeTo(summoner);

        new Thread(() -> {
            try {
                var res = DLWrapper.attemptAndWait(url);
                if (res != 0) System.out.println("DLWrapper exited with non-zero value: " + res);
            } catch (DLException e) {
                exception = e;
            } catch (IOException | InterruptedException e) {
                if (e instanceof IOException ioe && ioe.getMessage().startsWith("Cannot run program")) {
                    exception = new DLException("Could not open yt-dlp! (Is it installed?)");
                } else {
                    e.printStackTrace();
                    exception = new DLException();
                }
            }
            dispose();
        }).start();

        setVisible(true);
        if (exception != null) throw exception;
    }
}
