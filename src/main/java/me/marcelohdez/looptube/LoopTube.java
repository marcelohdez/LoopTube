package me.marcelohdez.looptube;

import com.github.weisj.darklaf.LafManager;

public class LoopTube {
    private static void installLookAndFeel() {
        LafManager.install(LafManager.themeForPreferredStyle(LafManager.getPreferredThemeStyle()));
    }

    public static void main(String[] args) {
        LafManager.enabledPreferenceChangeReporting(true); // track system dark mode changes
        installLookAndFeel();
        LafManager.addThemePreferenceChangeListener(e -> installLookAndFeel());

        var model = new AppModel();
        var view = new AppView();

        new AppController(model, view).begin();
    }
}
