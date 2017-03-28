package hr.fer.zemris.zavrsni.rts.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;

public abstract class AbstractGameScreen extends ScreenAdapter {

    protected final Game game;

    public AbstractGameScreen(Game game) {
        this.game = game;
    }
}
