package me.marcelohdez.looptube;

import javax.swing.*;
import java.awt.*;

public class AppView extends JFrame {
    private final JTable songsTable = new JTable();

    private final JButton trimSongButton = new JButton("✂");
    private final JButton addSongButton = new JButton("Add");
    private final JButton deleteSongButton = new JButton("Delete");
    private final JButton reloadSongsButton = new JButton("↻");

    private final JLabel nowPlayingLabel = new JLabel("No loop playing");
    private final JButton previousButton = new JButton("⏮");
    private final JButton pauseButton = new JButton("⏸");
    private final JButton skipButton = new JButton("⏭");

    // setup GUI
    public AppView() {
        setTitle("LoopTube");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addPlaylistPanel();
        addPlayingView();
    }

    public JTable getSongsTable() {
        return songsTable;
    }

    public JButton getAddSongButton() {
        return addSongButton;
    }

    public JButton getDeleteSongButton() {
        return deleteSongButton;
    }

    public JButton getTrimSongButton() {
        return trimSongButton;
    }

    public JButton getReloadSongsButton() {
        return reloadSongsButton;
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
        var editRow = new JPanel();
        editRow.add(trimSongButton);
        editRow.add(addSongButton);
        editRow.add(deleteSongButton);
        editRow.add(reloadSongsButton);

        var pnl = new JPanel(new BorderLayout());
        pnl.add(new JScrollPane(songsTable));
        pnl.add(editRow, BorderLayout.PAGE_END);
        add(pnl);
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
        add(view, BorderLayout.LINE_START);
    }
}
