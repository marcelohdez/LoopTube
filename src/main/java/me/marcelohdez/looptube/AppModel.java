package me.marcelohdez.looptube;

import me.marcelohdez.looptube.library.SongPlayer;

public class AppModel {
    private final LoopTableModel loopsListModel = new LoopTableModel();
    private final SongPlayer songPlayer = new SongPlayer();

    public LoopTableModel getLoopsListModel() {
        return loopsListModel;
    }

    public SongPlayer getSongPlayer() {
        return songPlayer;
    }
}
