package hr.fer.zemris.zavrsni.rts.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import hr.fer.zemris.zavrsni.rts.common.IGameSettings;

public abstract class AbstractGameScreen extends ScreenAdapter {

    protected final Game game;
    protected final IGameSettings gameSettings;

    public AbstractGameScreen(Game game, IGameSettings gameSettings) {
        this.game = game;
        this.gameSettings = gameSettings;
    }
}
