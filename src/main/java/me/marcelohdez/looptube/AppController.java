package me.marcelohdez.looptube;

import me.marcelohdez.looptube.dialog.AddSourceDialog;
import me.marcelohdez.looptube.dialog.DLProgressDialog;
import me.marcelohdez.looptube.dialog.ErrorDialog;
import me.marcelohdez.looptube.library.SongData;
import me.marcelohdez.looptube.ytdlp.DLException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

public record AppController(AppModel model, AppView view) {
    private static final String LOOP_TUBE_DIR = // end with a separator to indicate as directory
            System.getProperty("user.home") + File.separatorChar + ".LoopTube" + File.separator;
    public static final String LIBRARY_DIR = LOOP_TUBE_DIR + "library" + File.separator;

    public void begin() {
        System.out.printf("Starting LoopTube with library @ %s", LIBRARY_DIR);

        view.getLoopsTable().setModel(model.getLoopsListModel());
        view.getLoopsTable().addMouseListener(captureLoopSelections());
        view.getLoopsTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        view.getLoopsTable().getColumn(LoopTableModel.COL_NUM).setMaxWidth(30); // arbitrary; stops column being massive
        SwingUtilities.invokeLater(this::reloadSongs);

        // make initial table size comfortable
        var tableSize = view.getLoopsTable().getPreferredSize();
        view.getLoopsTable().setPreferredScrollableViewportSize(
                new Dimension((int)(tableSize.width * 1.5), tableSize.height)
        );

        view.getAddLoopButton().addActionListener(e -> addLoop());
        view.getDeleteLoopButton().addActionListener(e -> deleteLoop());
        view.getReloadLoopsButton().addActionListener(e -> reloadSongs());

        // set minimum size enough to fit everything:
        view.pack();
        view.setMinimumSize(view.getSize());

        view.setSize(400, 250);
        view.setLocationRelativeTo(null); // center on screen
        view.setVisible(true);
    }

    private void addLoop() {
        var maybeUrl = new AddSourceDialog(view).response();
        if (maybeUrl.isEmpty()) return;

        var url = maybeUrl.get();
        try {
            new DLProgressDialog(view).attempt(url);
        } catch (DLException | IOException ex) {
            new ErrorDialog(view, ex.getMessage());
        }

        SwingUtilities.invokeLater(this::reloadSongs);
    }

    private void deleteLoop() {
        var row = view.getLoopsTable().getSelectedRow();
        if (row < 0) return; // nothing selected

        var file = model.getLoopsListModel().get(row).getFile();

        try {
            Desktop.getDesktop().moveToTrash(file);
        } catch (UnsupportedOperationException ignored) {
            if (!file.delete()) { // attempt to delete permanently
                new ErrorDialog(view, "Could not delete " + file.getName());
            }
        }

        SwingUtilities.invokeLater(this::reloadSongs);
    }

    private void reloadSongs() {
        try {
            model.getLoopsListModel().clear();
            readSongsFromDisk();
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

                model.getLoopsListModel().add(maybeMp3.get());
            }
        }
    }

    private MouseListener captureLoopSelections() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                var row = view.getLoopsTable().getSelectedRow();
                if (row >= 0) view.getNowPlayingLabel().setText(model.getLoopsListModel().get(row).toString());
            }
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        };
    }
}
