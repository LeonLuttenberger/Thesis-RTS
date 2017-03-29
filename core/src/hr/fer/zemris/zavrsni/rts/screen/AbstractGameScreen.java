package hr.fer.zemris.zavrsni.rts.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;

public abstract class AbstractGameScreen extends ScreenAdapter {

    protected final Game game;
    private InputProcessor inputProcessor;

    public AbstractGameScreen(Game game) {
        this.game = game;
    }

    public InputProcessor getInputProcessor() {
           return inputProcessor;
    }

    protected void setInputProcessor(InputProcessor inputProcessor) {
        this.inputProcessor = inputProcessor;
        Gdx.input.setInputProcessor(inputProcessor);
    }
}
