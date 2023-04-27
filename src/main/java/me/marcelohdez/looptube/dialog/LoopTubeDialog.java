package me.marcelohdez.looptube.dialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

abstract class LoopTubeDialog extends JDialog {
    protected final Component summoner;
    protected final JLabel label = new JLabel();

    public LoopTubeDialog(Component summoner, String title, String text) {
        this.summoner = summoner;

        setTitle(title);
        setResizable(false);
        setModalityType(ModalityType.APPLICATION_MODAL);

        label.setText(text);
        label.setBorder(new EmptyBorder(6, 6, 6, 6));

        add(label, BorderLayout.PAGE_START);
    }
}
