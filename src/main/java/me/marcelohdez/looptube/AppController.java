package me.marcelohdez.looptube;

import me.marcelohdez.looptube.dialog.AddSourceDialog;
import me.marcelohdez.looptube.dialog.DLProgressDialog;
import me.marcelohdez.looptube.dialog.ErrorDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.Optional;

public record AppController(AppModel model, AppView view) {
    private static final String LOOP_TUBE_DIR = // end with a separator to indicate as directory
            System.getProperty("user.home") + File.separatorChar + ".LoopTube" + File.separatorChar;
    public static final String SOURCES_DIR = LOOP_TUBE_DIR + "sources" + File.separatorChar;
    public static final String LIBRARY_DIR = LOOP_TUBE_DIR + "library" + File.separatorChar;

    public void begin() {
        System.out.printf("Root @ %s\nSources @ %s\nLibrary @ %s%n", LOOP_TUBE_DIR, SOURCES_DIR, LIBRARY_DIR);

        view.getLoopsTable().setModel(model.getLoopsListModel());
        view.getLoopsTable().addMouseListener(captureLoopSelections());
        view.getLoopsTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        view.getLoopsTable().getColumn(LoopTableModel.COL_NUM).setMaxWidth(30); // arbitrary; stops column being massive
        SwingUtilities.invokeLater(this::reloadLoops);

        // make initial table size comfortable
        var tableSize = view.getLoopsTable().getPreferredSize();
        view.getLoopsTable().setPreferredScrollableViewportSize(
                new Dimension((int)(tableSize.width * 1.5), tableSize.height)
        );

        view.getAddLoopButton().addActionListener(e -> addLoop());
        view.getDeleteLoopButton().addActionListener(e -> deleteLoop());
        view.getReloadLoopsButton().addActionListener(e -> reloadLoops());

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
            var res = new DLProgressDialog(view).attempt(url);

            switch (res) {
                case InvalidURL -> new ErrorDialog(view, '\"' + url + "\" is not a valid URL!");
                case URLOpenFail -> new ErrorDialog(view, "Could not connect to URL! (Check your network?)");
                case CannotRunYTDLP -> new ErrorDialog(view, "Could not open yt-dlp! (Is it installed?)");
                case Error -> new ErrorDialog(view, "An error occurred getting your audio!");
            }
        } catch (IOException ex) {
            new ErrorDialog(view, ex.getMessage());
        }

        SwingUtilities.invokeLater(this::reloadLoops);
    }

    private void deleteLoop() {
        var row = view.getLoopsTable().getSelectedRow();
        if (row >= 0) model.getLoopsListModel().remove(row);
    }

    private void reloadLoops() {
        try {
            model.getLoopsListModel().clear();
            readLoopsFromDir(SOURCES_DIR);
            readLoopsFromDir(LIBRARY_DIR);
        } catch (NoSuchFileException ex) { // paths have not been created
            System.out.println('\n' + ex.getFile() + " has not been created, not reading loops.");
        } catch (IOException ex) {
            ex.printStackTrace();
            new ErrorDialog(view, "Could not read from library directory! I/O Exception.");
        }

        view.getLoopsTable().repaint();
    }

    private void readLoopsFromDir(final String dir) throws IOException {
        var loopsDir = new File(dir);
        try (var dirStream = Files.newDirectoryStream(loopsDir.toPath())) {
            for (var file : dirStream) {
                var title = getMP3Title(file.getFileName().toString());
                if (title.isEmpty()) continue; // skip non mp3's

                model.getLoopsListModel().add(title.get());
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
                if (row >= 0) view.getNowPlayingLabel().setText(model.getLoopsListModel().get(row));
            }
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        };
    }

    private static Optional<String> getMP3Title(String filename) {
        final var ext = ".mp3";
        if (!filename.endsWith(ext)) return Optional.empty();

        return Optional.of(filename.substring(0, filename.length() - ext.length()));
    }
}
