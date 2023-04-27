package me.marcelohdez.looptube.dialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

abstract class LoopTubeDialog extends JDialog {
    private static final int WORDS_PER_LINE = 7;

    protected final Component summoner;
    protected final JTextArea label = new JTextArea();

    public LoopTubeDialog(Component summoner, String title, String text) {
        this.summoner = summoner;

        setTitle(title);
        setResizable(false);
        setModalityType(ModalityType.APPLICATION_MODAL);

        label.setText(makeParagraph(text));
        label.setBorder(new EmptyBorder(4, 4, 4, 4));
        label.setEditable(false);

        add(label, BorderLayout.PAGE_START);
    }

    private static String makeParagraph(String s) {
        var words = s.split(" ");
        if (words.length > WORDS_PER_LINE) {
            for (int i = WORDS_PER_LINE; i < words.length; i += WORDS_PER_LINE) {
                words[i] += '\n';
            }
        }

        return String.join(" ", words);
    }
}
