package me.marcelohdez.looptube;

public record AppController(AppModel model, AppView view) {
    public void begin() {
        view.getLoopsList().setModel(model.getLoopsListModel());

        // set minimum size enough to fit everything:
        view.pack();
        view.setMinimumSize(view.getSize());

        for (int i = 1; i <= 3; i++) { // dummy loops
            model.getLoopsListModel().add("Loop " + i);
        }
        view.getPlaylistLabel().setText("My Playlist");
        view.getLabel().setText("Hello!");

        view.setSize(400, 250);
        view.setVisible(true);
    }
}
