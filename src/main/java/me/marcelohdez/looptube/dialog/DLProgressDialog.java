package me.marcelohdez.looptube.dialog;

import me.marcelohdez.looptube.ytdlp.DLResult;
import me.marcelohdez.looptube.ytdlp.DLWrapper;

import java.awt.*;
import java.io.IOException;

public class DLProgressDialog extends LoopTubeDialog {
    private final Component summoner;
    private DLResult result;

    public DLProgressDialog(Component summoner) {
        super("Progress", "A download is in progress..");
        this.summoner = summoner;
        pack();
    }

    public DLResult attempt(final String url) throws IOException {
        setLocationRelativeTo(summoner);

        new Thread(() -> {
            var wrapper = new DLWrapper(url);
            try {
                if (wrapper.startAndWait() != 0) {
                    System.out.println("DLWrapper exited with non-zero value... ");
                    result = DLResult.Error;
                } else result = wrapper.getResult();
            } catch (IOException | InterruptedException e) {
                if (e instanceof IOException ioe && ioe.getMessage().startsWith("Cannot run program")) {
                    result = DLResult.CannotRunYTDLP;
                } else {
                    e.printStackTrace();
                    result = DLResult.Error;
                }
            }
            dispose();
        }).start();

        setVisible(true);
        return result;
    }
}
