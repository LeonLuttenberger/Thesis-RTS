package hr.fer.zemris.zavrsni.rts.screen;

import com.badlogic.gdx.Game;
import hr.fer.zemris.zavrsni.rts.common.Constants;
import hr.fer.zemris.zavrsni.rts.common.GameSettings;
import hr.fer.zemris.zavrsni.rts.common.IGameSettings;

public abstract class ScreenManagedGame extends Game {

    protected ScreenManager screenManager;
    protected IGameSettings settings;

    @Override
    public void create() {
        settings = new GameSettings(Constants.SETTINGS);
        settings.load();

        screenManager = new ScreenManager(this, settings);
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    @Override
    public void dispose() {
        super.dispose();

        screenManager.dispose();
    }
}
