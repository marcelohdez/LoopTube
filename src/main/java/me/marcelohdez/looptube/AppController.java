package me.marcelohdez.looptube;

import javazoom.jl.decoder.JavaLayerException;
import me.marcelohdez.looptube.dialog.*;
import me.marcelohdez.looptube.ffmpeg.TrimException;
import me.marcelohdez.looptube.library.SongData;
import me.marcelohdez.looptube.library.SongEventListener;
import me.marcelohdez.looptube.library.SongsTableModel;
import me.marcelohdez.looptube.ytdlp.DLException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.text.ParseException;

public record AppController(AppModel model, AppView view) implements MouseListener, SongEventListener {
    private static final int MAX_NAME_CHAR = 40;
    private static final String LOOP_TUBE_DIR = // end with a separator to indicate as directory
            System.getProperty("user.home") + File.separatorChar + ".LoopTube" + File.separator;
    public static final String LIBRARY_DIR = LOOP_TUBE_DIR + "library" + File.separator;

    public void begin() {
        System.out.printf("Starting LoopTube with library @ %s\n", LIBRARY_DIR);

        view.getSongsTable().setModel(model.getSongsTableModel());
        view.getSongsTable().addMouseListener(this);
        view.getSongsTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        view.getSongsTable().getColumn(SongsTableModel.COL_NUM).setMaxWidth(30); // arbitrary; stops column being massive
        SwingUtilities.invokeLater(this::reloadSongs);

        attachActionListeners();
        model.getSongPlayer().setSongEventListener(this);

        // let table be shrunken
        view.getSongsTable().setPreferredScrollableViewportSize(null);
        // set minimum size enough to fit everything:
        view.pack();
        view.setMinimumSize(view.getSize());

        view.setSize(view.getWidth() + 60, view.getHeight() + 120); // set default size more comfortable
        view.setLocationRelativeTo(null); // center on screen
        view.setVisible(true);
    }

    private void attachActionListeners() {
        view.getPreviousButton().addActionListener(e -> previousSong());
        view.getPauseButton().addActionListener(e -> pauseOrPlay());
        view.getSkipButton().addActionListener(e -> nextSong());

        view.getAddSongButton().addActionListener(e -> addSong());
        view.getDeleteSongButton().addActionListener(e -> deleteSong());
        view.getTrimSongButton().addActionListener(e -> trimSong());
        view.getReloadSongsButton().addActionListener(e -> reloadSongs());

        view.getLoopButton().addActionListener(e ->
            model.getSongPlayer().setRepeating(view.getLoopButton().isSelected())
        );
    }

    private void previousSong() {
        try {
            if (!model.getSongPlayer().previous()) return;

            var maybeFile = model.getSongPlayer().getSource();
            if (maybeFile.isEmpty()) return;

            var playingIndex = findSongFileIndex(maybeFile.get());
            if (playingIndex == -1) return;

            var songList = model.getSongsTableModel();
            // get previous track, or loop to last track
            if (playingIndex > 0) {
                playNewSong(songList.get(playingIndex - 1));
            } else playNewSong(songList.get(songList.getRowCount() - 1));
        } catch (IOException | JavaLayerException e) {
            e.printStackTrace();
            new ErrorDialog(view, "Oops! Could not rewind.");
        }
    }

    private void nextSong() {
        var maybeFile = model.getSongPlayer().getSource();
        if (maybeFile.isEmpty()) return;

        var playingIndex = findSongFileIndex(maybeFile.get());
        if (playingIndex == -1) return;

        var songsList = model.getSongsTableModel();
        if (playingIndex == songsList.getRowCount() - 1) {
            playNewSong(songsList.get(0));
        } else {
            playNewSong(songsList.get(playingIndex + 1));
        }
    }

    /**
     * Will find a matching song file in the model's songs list.
     * @return the index of the song found, -1 otherwise.
     */
    private int findSongFileIndex(File f) {
        var songList = model.getSongsTableModel();

        for (int i = 0; i < songList.getRowCount(); i++) {
            if (songList.get(i).getFile() == f) {
                return i;
            }
        }

        return -1;
    }

    private void playNewSong(SongData song) {
        // do not replay current song when clicked:
        var player = model.getSongPlayer();
        if (player.getSource().isPresent() && song.getFile() == player.getSource().get()) return;

        try {
            player.setSource(song.getFile());

            selectSourceInTable(song.getFile());
            setPlayerTitleTo(song.toString());
            pauseOrPlay();
        } catch (IOException | JavaLayerException e) {
            e.printStackTrace();
            new ErrorDialog(view, "Oops! Could not play file.");
        }
    }

    /** Will make the view's songs table select the index with the given file as source */
    private void selectSourceInTable(File source) {
        var nowPlayingIndex = findSongFileIndex(source);
        view.getSongsTable().setRowSelectionInterval(nowPlayingIndex, nowPlayingIndex);
    }

    /** Will set the player title to the given text, if too long it will shrink it */
    private void setPlayerTitleTo(String title) {
        // will split long titles like "this is my long title" -> "this is ...ng title"
        if (title.length() > MAX_NAME_CHAR) {
            final var tweener = "...";
            final var limit = (MAX_NAME_CHAR - tweener.length()) / 2; // max chars on each half
            title = title.substring(0, limit) + tweener + title.substring(title.length() - limit);
        }

        view.getNowPlayingLabel().setText(title);
    }

    private void pauseOrPlay() {
        try {
            var player = model.getSongPlayer();
            if (player.isPlaying()) {
                player.stop();
            } else {
                player.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorDialog(view, "Oops! Could not play/pause song.");
        }
    }

    private void addSong() {
        var maybeUrl = new AddSongDialog(view).response();
        if (maybeUrl.isEmpty()) return;

        var url = maybeUrl.get();
        try {
            new DLProgressDialog(view).attempt(url);
        } catch (DLException | IOException ex) {
            new ErrorDialog(view, ex.getMessage());
        }

        SwingUtilities.invokeLater(this::reloadSongs);
    }

    private void deleteSong() {
        var row = view.getSongsTable().getSelectedRow();
        if (row < 0) return; // nothing selected

        var song = model.getSongsTableModel().get(row);
        var res = new AcceptDialog(view, "Are you sure you want to delete \"" + song + "\"?").response();
        if (!res) return;

        try {
            Desktop.getDesktop().moveToTrash(song.getFile());
        } catch (UnsupportedOperationException ignored) {
            if (!song.getFile().delete()) { // attempt to delete permanently
                new ErrorDialog(view, "Could not delete " + song);
            }
        }

        SwingUtilities.invokeLater(this::reloadSongs);
    }

    private void trimSong() {
        var row = view.getSongsTable().getSelectedRow();
        if (row < 0) return;

        try {
            var res = new TrimDialog(view, model.getSongsTableModel().get(row)).response();
            if (res) reloadSongs();
        } catch (ParseException | TrimException e) {
            e.printStackTrace();
            new ErrorDialog(view, e.getMessage());
        }
    }

    private void reloadSongs() {
        try {
            model.getSongsTableModel().clear();
            readSongsFromDisk();
            model.getSongsTableModel().sortAlphabetically();
        } catch (NoSuchFileException ex) { // paths have not been created
            System.out.println('\n' + ex.getFile() + " has not been created, not reading loops.");
        } catch (IOException ex) {
            ex.printStackTrace();
            new ErrorDialog(view, "Could not read from library directory! I/O Exception.");
        }
    }

    private void readSongsFromDisk() throws IOException {
        var loopsDir = new File(LIBRARY_DIR);
        try (var dirStream = Files.newDirectoryStream(loopsDir.toPath())) {
            for (var path : dirStream) {
                var maybeMp3 = SongData.from(path.toFile());
                if (maybeMp3.isEmpty()) continue; // skip non mp3's

                model.getSongsTableModel().add(maybeMp3.get());
            }
        }
    }

    @Override
    public void songIsPlaying() {
        view.getPauseButton().setText(AppView.ICON_PLAYING);
    }

    @Override
    public void songStopped() {
        view.getPauseButton().setText(AppView.ICON_PAUSED);
    }

    @Override
    public void songFinished() {
        nextSong();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON1) return;

        var row = view.getSongsTable().rowAtPoint(e.getPoint());
        if (row < 0) return;

        playNewSong(model.getSongsTableModel().get(row));
    }
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
