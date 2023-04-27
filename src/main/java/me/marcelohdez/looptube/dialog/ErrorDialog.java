package me.marcelohdez.looptube.dialog;

import javax.swing.*;
import java.awt.*;

public class ErrorDialog extends LoopTubeDialog {
    public ErrorDialog(Component summoner, String msg) {
        super(summoner, "Oh no!", msg);

        var button = new JButton("Ok");
        button.addActionListener(e -> dispose());

        add(button, BorderLayout.PAGE_END);
        pack();

        setLocationRelativeTo(summoner);
        setVisible(true);
    }
}
