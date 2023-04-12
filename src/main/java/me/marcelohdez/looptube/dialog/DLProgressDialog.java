package me.marcelohdez.looptube.dialog;

import me.marcelohdez.looptube.ytdlp.DLResult;
import me.marcelohdez.looptube.ytdlp.DLWrapper;

import javax.swing.*;
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

    public DLResult attempt(String url) throws IOException {
        setLocationRelativeTo(summoner);

        new SwingWorker<Integer, Boolean>() {
            final DLWrapper wrapper = new DLWrapper(url);
            @Override
            protected Integer doInBackground() throws IOException, InterruptedException {
                return wrapper.startAndWait();
            }
            @Override
            public void done() {
                try {
                    result = wrapper.getResult();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                dispose();
            }
        }.execute();

        setVisible(true);
        return result;
    }
}
