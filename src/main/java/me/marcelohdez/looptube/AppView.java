package me.marcelohdez.looptube;

import javax.swing.*;
import java.awt.*;

public class AppView extends JFrame {
    public static final String ICON_PAUSED = "▶";
    public static final String ICON_PLAYING = "⏸";

    private final JTable songsTable = new JTable();

    private final JButton trimSongButton = new JButton("✂");
    private final JButton addSongButton = new JButton("+");
    private final JButton deleteSongButton = new JButton("-");
    private final JButton reloadSongsButton = new JButton("↻");

    private final JLabel nowPlayingLabel = new JLabel("Nothing is playing");
    private final JButton previousButton = new JButton("⏮");
    private final JButton pauseButton = new JButton(ICON_PAUSED);
    private final JButton skipButton = new JButton("⏭");

    private final JToggleButton loopButton = new JToggleButton("∞");

    // setup GUI
    public AppView() {
        setTitle("LoopTube");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setToolTips();
        add(playingButtonsPanel(), BorderLayout.PAGE_START);
        add(new JScrollPane(songsTable));
        add(playlistButtonsPanel(), BorderLayout.PAGE_END);
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

    public JToggleButton getLoopButton() {
        return loopButton;
    }

    private void setToolTips() {
        trimSongButton.setToolTipText("Trim song");
        addSongButton.setToolTipText("Add new song");
        deleteSongButton.setToolTipText("Delete song");
        reloadSongsButton.setToolTipText("Reload library from disk");

        previousButton.setToolTipText("Rewind/previous track");
        pauseButton.setToolTipText("Pause track");
        skipButton.setToolTipText("Skip track");
        loopButton.setToolTipText("Loop track");
    }

    private JPanel playlistButtonsPanel() {
        var buttonsRow = new JPanel();
        buttonsRow.add(trimSongButton);
        buttonsRow.add(addSongButton);
        buttonsRow.add(deleteSongButton);
        buttonsRow.add(reloadSongsButton);

        return buttonsRow;
    }

    private JPanel playingButtonsPanel() {
        var view = new JPanel(new BorderLayout());

        var labelRow = new JPanel();
        labelRow.add(nowPlayingLabel);

        var playlistButtonsRow = new JPanel();
        playlistButtonsRow.add(previousButton);
        playlistButtonsRow.add(pauseButton);
        playlistButtonsRow.add(skipButton);
        playlistButtonsRow.add(loopButton);

        view.add(labelRow, BorderLayout.PAGE_START);
        view.add(playlistButtonsRow, BorderLayout.PAGE_END);
        return view;
    }
}
