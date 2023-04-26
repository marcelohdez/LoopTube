package me.marcelohdez.looptube;

import me.marcelohdez.looptube.library.SongPlayer;

public class AppModel {
    private final SongsTableModel songsTableModel = new SongsTableModel();
    private final SongPlayer songPlayer = new SongPlayer();

    public SongsTableModel getSongsTableModel() {
        return songsTableModel;
    }

    public SongPlayer getSongPlayer() {
        return songPlayer;
    }
}
