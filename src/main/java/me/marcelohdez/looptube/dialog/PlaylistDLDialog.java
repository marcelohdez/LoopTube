package me.marcelohdez.looptube.dialog;

import javax.swing.*;
import java.awt.*;

public class PlaylistDLDialog extends LoopTubeDialog {
    public enum DLChoice {
        ALL, ONE, NONE
    }

    public DLChoice response = DLChoice.NONE;

    public PlaylistDLDialog(Component summoner) {
        super(summoner, "Download playlist", "This is a playlist URL. What would you like to download?");

        var downloadAll = new JButton("All audios");
        downloadAll.addActionListener(e -> {
            response = DLChoice.ALL;
            dispose();
        });

        var downloadThis = new JButton("Only this audio");
        downloadThis.addActionListener(e -> {
            response = DLChoice.ONE;
            dispose();
        });

        var downloadNone = new JButton("None");
        downloadNone.addActionListener(e -> dispose());

        var pnl = new JPanel();
        pnl.add(downloadAll);
        pnl.add(downloadThis);
        pnl.add(downloadNone);

        add(pnl);
        pack();
    }

    public DLChoice response() {
        setLocationRelativeTo(summoner);
        setVisible(true);

        return response;
    }
}
