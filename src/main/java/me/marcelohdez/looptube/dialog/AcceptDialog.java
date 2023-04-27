package me.marcelohdez.looptube.dialog;

import javax.swing.*;
import java.awt.*;

public class AcceptDialog extends LoopTubeDialog {
    private boolean response = false;

    public AcceptDialog(Component summoner, String request) {
        super(summoner, "Accept", request);

        var pnl = new JPanel();
        var okButton = new JButton("Ok");
        okButton.addActionListener(e -> {
            response = true;
            dispose();
        });
        var cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        pnl.add(okButton);
        pnl.add(cancelButton);
        add(pnl);
        pack();
    }

    public boolean response() {
        setLocationRelativeTo(summoner);
        setVisible(true);

        return response;
    }
}
