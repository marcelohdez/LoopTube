package me.marcelohdez.looptube.dialog;

import javax.swing.*;
import java.awt.*;

public class ErrorDialog extends LoopTubeDialog {
    private static final int WORDS_PER_LINE = 7;

    public ErrorDialog(Component summoner, String msg) {
        super(summoner, "Oh no!", "Error descriptor:");

        var msgArea = new JTextArea(makeParagraph(msg));
        msgArea.setEditable(false);

        var button = new JButton("Ok");
        button.addActionListener(e -> dispose());

        add(msgArea);
        add(button, BorderLayout.PAGE_END);
        pack();

        setLocationRelativeTo(summoner);
        setVisible(true);
    }

    private String makeParagraph(String s) {
        var words = s.split(" ");
        if (words.length > WORDS_PER_LINE) {
            for (int i = WORDS_PER_LINE; i < words.length; i += WORDS_PER_LINE) {
                words[i] += '\n';
            }
        }

        return String.join(" ", words);
    }
}
