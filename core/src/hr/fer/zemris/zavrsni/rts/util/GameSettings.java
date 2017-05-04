package hr.fer.zemris.zavrsni.rts.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameSettings implements IGameSettings {

    private static final String KEY_FPS_COUNTER = "showFPSCounter";

    private boolean showFPSCounter = false;

    private final Preferences preferences;

    public GameSettings(String preferencesFile) {
        preferences = Gdx.app.getPreferences(preferencesFile);
    }

    @Override
    public void load() {
        showFPSCounter = preferences.getBoolean(KEY_FPS_COUNTER, false);
    }

    @Override
    public void save() {
        preferences.putBoolean(KEY_FPS_COUNTER, showFPSCounter);

        preferences.flush();
    }

    @Override
    public boolean showFPSCounter() {
        return showFPSCounter;
    }

    @Override
    public void setShowFPSCounter(boolean showFPSCounter) {
        this.showFPSCounter = showFPSCounter;
    }
}
