package me.marcelohdez.looptube;

import javax.swing.*;
import java.awt.*;

public class AppView extends JFrame {
    private final JLabel playlistLabel = new JLabel();
    private final JTable loopsList = new JTable();
    private final JLabel label = new JLabel();

    // setup GUI
    public AppView() {
        setTitle("LoopTube");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        var headingSize = playlistLabel.getFont().getSize() * 1.2f; // increase by 20%
        playlistLabel.setFont(playlistLabel.getFont().deriveFont(headingSize));
        playlistLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        var pnl = new JPanel(new BorderLayout());
        pnl.add(playlistLabel, BorderLayout.PAGE_START);
        pnl.add(loopsList);

        add(pnl, BorderLayout.LINE_START);
        add(label);
    }

    public JLabel getPlaylistLabel() {
        return playlistLabel;
    }

    public JTable getLoopsList() {
        return loopsList;
    }

    public JLabel getLabel() {
        return label;
    }
}
