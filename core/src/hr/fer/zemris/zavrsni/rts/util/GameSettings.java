package hr.fer.zemris.zavrsni.rts.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameSettings implements IGameSettings {

    private static final String KEY_FPS_COUNTER = "showFPSCounter";
    private static final String KEY_PLAYER_HEALTH_BAR = "showPlayerUnitHealthBar";
    private static final String KEY_HOSTILE_HEALTH_BAR = "showHostileUnitHealthBar";
    private static final String KEY_RESOURCE_HEALTH_BAR = "showHostileUnitHealthBar";
    private static final String KEY_BUILDING_HEALTH_BAR = "showHostileUnitHealthBar";

    private boolean showFPSCounter = false;
    private boolean showPlayerUnitHealthBars = true;
    private boolean showHostileUnitHealthBars = true;
    private boolean showResourceHealthBars = false;
    private boolean showBuildingHealthBars = true;

    private final Preferences preferences;

    public GameSettings(String preferencesFile) {
        preferences = Gdx.app.getPreferences(preferencesFile);
    }

    @Override
    public void load() {
        showFPSCounter = preferences.getBoolean(KEY_FPS_COUNTER, false);
        showPlayerUnitHealthBars = preferences.getBoolean(KEY_PLAYER_HEALTH_BAR, true);
        showHostileUnitHealthBars = preferences.getBoolean(KEY_HOSTILE_HEALTH_BAR, true);
        showResourceHealthBars = preferences.getBoolean(KEY_RESOURCE_HEALTH_BAR, false);
        showBuildingHealthBars = preferences.getBoolean(KEY_BUILDING_HEALTH_BAR, true);
    }

    @Override
    public void save() {
        preferences.putBoolean(KEY_FPS_COUNTER, showFPSCounter);
        preferences.putBoolean(KEY_PLAYER_HEALTH_BAR, showPlayerUnitHealthBars);
        preferences.putBoolean(KEY_HOSTILE_HEALTH_BAR, showHostileUnitHealthBars);
        preferences.putBoolean(KEY_RESOURCE_HEALTH_BAR, showResourceHealthBars);
        preferences.putBoolean(KEY_BUILDING_HEALTH_BAR, showBuildingHealthBars);

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

    @Override
    public boolean showResourceHealthBars() {
        return showResourceHealthBars;
    }

    @Override
    public void setShowResourceHealthBars(boolean showResourceHealthBars) {
        this.showResourceHealthBars = showResourceHealthBars;
    }

    @Override
    public boolean showBuildingHealthBars() {
        return showResourceHealthBars;
    }

    @Override
    public void setShowBuildingHealthBars(boolean showBuildingHealthBars) {
        this.showBuildingHealthBars = showBuildingHealthBars;
    }
}
