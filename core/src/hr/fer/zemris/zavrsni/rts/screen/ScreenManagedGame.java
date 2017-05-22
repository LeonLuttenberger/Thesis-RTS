package hr.fer.zemris.zavrsni.rts.screen;

import com.badlogic.gdx.Game;

public abstract class ScreenManagedGame extends Game {

    private final ScreenManager screenManager = new ScreenManager(this);

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    @Override
    public void dispose() {
        super.dispose();

        screenManager.dispose();
    }
}
