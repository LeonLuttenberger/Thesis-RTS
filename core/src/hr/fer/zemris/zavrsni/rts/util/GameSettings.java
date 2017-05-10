package hr.fer.zemris.zavrsni.rts.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameSettings implements IGameSettings {

    private static final String KEY_FPS_COUNTER = "showFPSCounter";
    private static final String KEY_PLAYER_HEALTH_BAR = "showPlayerUnitHealthBar";
    private static final String KEY_HOSTILE_HEALTH_BAR = "showHostileUnitHealthBar";

    private boolean showFPSCounter = false;
    private boolean showPlayerUnitHealthBars = true;
    private boolean showHostileUnitHealthBars = true;

    private final Preferences preferences;

    public GameSettings(String preferencesFile) {
        preferences = Gdx.app.getPreferences(preferencesFile);
    }

    @Override
    public void load() {
        showFPSCounter = preferences.getBoolean(KEY_FPS_COUNTER, false);
        showPlayerUnitHealthBars = preferences.getBoolean(KEY_PLAYER_HEALTH_BAR, true);
        showHostileUnitHealthBars = preferences.getBoolean(KEY_HOSTILE_HEALTH_BAR, true);
    }

    @Override
    public void save() {
        preferences.putBoolean(KEY_FPS_COUNTER, showFPSCounter);
        preferences.putBoolean(KEY_PLAYER_HEALTH_BAR, showPlayerUnitHealthBars);
        preferences.putBoolean(KEY_HOSTILE_HEALTH_BAR, showHostileUnitHealthBars);

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

    @Override
    public boolean showPlayerUnitHealthBars() {
        return showPlayerUnitHealthBars;
    }

    @Override
    public void setShowPlayerUnitHealthBars(boolean showPlayerUnitHealthBars) {
        this.showPlayerUnitHealthBars = showPlayerUnitHealthBars;
    }

    @Override
    public boolean showHostileUnitHealthBars() {
        return showHostileUnitHealthBars;
    }

    @Override
    public void setShowHostileUnitHealthBars(boolean showHostileUnitHealthBars) {
        this.showHostileUnitHealthBars = showHostileUnitHealthBars;
    }
}
