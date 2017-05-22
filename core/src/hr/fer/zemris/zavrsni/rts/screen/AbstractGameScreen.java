package hr.fer.zemris.zavrsni.rts.screen;

import com.badlogic.gdx.ScreenAdapter;
import hr.fer.zemris.zavrsni.rts.common.IGameSettings;

public abstract class AbstractGameScreen extends ScreenAdapter {

    protected final ScreenManagedGame game;
    protected final IGameSettings gameSettings;

    public AbstractGameScreen(ScreenManagedGame game, IGameSettings gameSettings) {
        this.game = game;
        this.gameSettings = gameSettings;
    }
}
