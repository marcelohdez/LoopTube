package me.marcelohdez.looptube.dialog;

import me.marcelohdez.looptube.ytdlp.SourceURLVerifier;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class AddSourceDialog extends LoopTubeDialog {
    private final Component summoner;
    private final SourceURLVerifier verifier = new SourceURLVerifier();
    private final JTextField responseField = new JTextField();

    public AddSourceDialog(Component summoner) {
        super("Add Loop Source", "Please enter a YouTube video link:");

        this.summoner = summoner;

        var cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            responseField.setText("");
            dispose();
        });

        var acceptButton = new JButton("Accept");
        acceptButton.addActionListener(e -> {
            if (!verifier.verify(responseField)) {
                label.setText("Invalid URL, try again.");
                return;
            }

            dispose();
        });

        var buttonRow = new JPanel();
        buttonRow.add(acceptButton);
        buttonRow.add(cancelButton);

        add(responseField);
        add(buttonRow, BorderLayout.PAGE_END);
        pack();
    }

    public Optional<String> response() {
        setLocationRelativeTo(summoner);
        setVisible(true);

        return responseField.getText().isEmpty() ? Optional.empty() : Optional.of(responseField.getText());
    }
}
