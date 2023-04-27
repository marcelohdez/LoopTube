package me.marcelohdez.looptube.dialog;

import me.marcelohdez.looptube.ytdlp.SourceURLVerifier;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class AddSongDialog extends LoopTubeDialog {
    private final SourceURLVerifier verifier = new SourceURLVerifier();
    private final JTextField responseField = new JTextField();

    public AddSongDialog(Component summoner) {
        super(summoner, "Add song", "Please enter a YouTube video/playlist link:");

        var cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            responseField.setText("");
            dispose();
        });

        var acceptButton = new JButton("Accept");
        acceptButton.addActionListener(e -> {
            if (verifier.verify(responseField)) {
                var videoFromPlaylist = verifier.getVideoURLOnly(responseField);
                // let user choose to download all, only this one, or no videos if link is playlist:
                if (videoFromPlaylist.isPresent()) {
                    var res = new PlaylistDLDialog(this).response();

                    switch (res) {
                        case ALL -> {
                            var accept = new AcceptDialog(
                                    this,
                                    "Are you sure you want to download all audios? This may take a while!"
                            ).response();

                            if (!accept) return;
                        }
                        case ONE -> responseField.setText(videoFromPlaylist.get());
                        case NONE -> {
                            return;
                        }
                    }
                }

                dispose();
            }

            label.setText("Invalid URL, try again.");
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
