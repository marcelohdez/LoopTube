package me.marcelohdez.looptube;

import javax.swing.*;
import java.awt.*;

public class AppView extends JFrame {
    private final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    private final JLabel playlistLabel = new JLabel();
    private final JTable loopsTable = new JTable();

    private final JButton addLoopButton = new JButton("Add");
    private final JButton deleteLoopButton = new JButton("Delete");
    private final JButton reloadLoopsButton = new JButton("↻");

    private final JLabel nowPlayingLabel = new JLabel("No loop playing");
    private final JButton previousButton = new JButton("⏮");
    private final JButton pauseButton = new JButton("⏸️");
    private final JButton skipButton = new JButton("⏭");

    // setup GUI
    public AppView() {
        setTitle("LoopTube");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addPlaylistPanel();
        addPlayingView();
        add(splitPane);
    }

    public JLabel getPlaylistLabel() {
        return playlistLabel;
    }

    public JTable getLoopsTable() {
        return loopsTable;
    }

    public JButton getAddLoopButton() {
        return addLoopButton;
    }

    public JButton getDeleteLoopButton() {
        return deleteLoopButton;
    }

    public JButton getReloadLoopsButton() {
        return reloadLoopsButton;
    }

    public JLabel getNowPlayingLabel() {
        return nowPlayingLabel;
    }

    public JButton getPreviousButton() {
        return previousButton;
    }

    public JButton getPauseButton() {
        return pauseButton;
    }

    public JButton getSkipButton() {
        return skipButton;
    }

    private void addPlaylistPanel() {
        var headingSize = playlistLabel.getFont().getSize() * 1.2f; // increase by 20%
        playlistLabel.setFont(playlistLabel.getFont().deriveFont(headingSize));
        playlistLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        var editRow = new JPanel();
        editRow.add(addLoopButton);
        editRow.add(deleteLoopButton);
        editRow.add(reloadLoopsButton);

        var pnl = new JPanel(new BorderLayout());
        pnl.add(playlistLabel, BorderLayout.PAGE_START);
        pnl.add(new JScrollPane(loopsTable));
        pnl.add(editRow, BorderLayout.PAGE_END);

        splitPane.add(pnl);
    }

    private void addPlayingView() {
        var view = new JPanel();
        view.setLayout(new BoxLayout(view, BoxLayout.PAGE_AXIS));

        var buttonRow = new JPanel();
        buttonRow.add(previousButton);
        buttonRow.add(pauseButton);
        buttonRow.add(skipButton);

        var labelRow = new JPanel();
        labelRow.add(nowPlayingLabel);

        view.add(labelRow);
        view.add(buttonRow);
        splitPane.add(view);
    }
}
