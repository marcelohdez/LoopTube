package me.marcelohdez.looptube;

import me.marcelohdez.looptube.dialog.AddSourceDialog;
import me.marcelohdez.looptube.dialog.DLProgressDialog;
import me.marcelohdez.looptube.dialog.ErrorDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public record AppController(AppModel model, AppView view) {
    public void begin() {
        view.getLoopsTable().setModel(model.getLoopsListModel());
        view.getLoopsTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        view.getLoopsTable().getColumn(LoopTableModel.COL_NUM).setMaxWidth(30); // arbitrary; stops column being massive

        // make initial table size comfortable
        var tableSize = view.getLoopsTable().getPreferredSize();
        view.getLoopsTable().setPreferredScrollableViewportSize(
                new Dimension((int)(tableSize.width * 1.5), tableSize.height)
        );

        view.getAddLoopButton().addActionListener(e -> addLoop());
        view.getDeleteLoopButton().addActionListener(e -> deleteLoop());
        view.getLoopsTable().addMouseListener(captureLoopSelections());

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
                case Error -> new ErrorDialog(view, "An error occurred getting your video!");
            }
        } catch (IOException ex) {
            new ErrorDialog(view, ex.getMessage());
        }
    }

    private void deleteLoop() {
        var row = view.getLoopsTable().getSelectedRow();
        if (row >= 0) model.getLoopsListModel().remove(row);
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
}
