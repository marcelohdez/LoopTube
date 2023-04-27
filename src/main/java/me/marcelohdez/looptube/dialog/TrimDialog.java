package me.marcelohdez.looptube.dialog;

import me.marcelohdez.looptube.ffmpeg.Duration;
import me.marcelohdez.looptube.ffmpeg.TrimException;
import me.marcelohdez.looptube.library.SongData;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;

public class TrimDialog extends LoopTubeDialog {
    private static final String FORMAT = "#:##";

    private final Component summoner;
    private final SongData song;
    private final JFormattedTextField startField = new JFormattedTextField(new MaskFormatter(FORMAT));
    private final JFormattedTextField endField = new JFormattedTextField(new MaskFormatter(FORMAT));

    public TrimDialog(Component summoner, SongData song) throws ParseException {
        super("Trimming \"%s\"".formatted(song), "Select the range you would like to keep:");
        this.summoner = summoner;
        this.song = song;

        startField.setColumns(3);
        endField.setColumns(3);

        var timeFieldsRow = new JPanel();
        timeFieldsRow.add(startField);
        timeFieldsRow.add(endField);

        add(timeFieldsRow);
        pack();
    }

    public boolean response() throws TrimException {
        setLocationRelativeTo(summoner);
        setVisible(true);

        if (startField.isEditValid() && endField.isEditValid()) {
            return new FFMPEGDialog(this)
                    .attempt(song, Duration.from(startField.getText()), Duration.from(endField.getText()));
        }

        return true;
    }
}
