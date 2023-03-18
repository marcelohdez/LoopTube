package me.marcelohdez.looptube.dialog;

import me.marcelohdez.looptube.ytdlp.SourceURLVerifier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Optional;

public class AddSourceDialog extends LoopTubeDialog implements WindowListener {
    private final Component summoner;
    private final SourceURLVerifier verifier = new SourceURLVerifier();
    private final JTextField responseField = new JTextField();

    public AddSourceDialog(Component summoner) {
        super("Add Loop Source", "Please enter a YouTube video link:");

        this.summoner = summoner;
        addWindowListener(this);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

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

        if (!responseField.getText().isEmpty()) {
            return Optional.of(responseField.getText());
        }

        return Optional.empty();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        dispose();
    }

    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}
}
