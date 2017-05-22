package hr.fer.zemris.zavrsni.rts.screen;

import com.badlogic.gdx.Game;
import com.sun.media.jfxmediaimpl.MediaDisposer.Disposable;

import java.util.Stack;

public class ScreenManager implements Disposable {

    private final Game game;
    private final Stack<AbstractGameScreen> gameScreenStack = new Stack<>();

    public ScreenManager(Game game) {
        this.game = game;
    }

    public void pushScreen(AbstractGameScreen gameScreen) {
        gameScreenStack.push(gameScreen);

        game.setScreen(gameScreen);
    }

    public void popScreen() {
        popScreen(1);
    }

    public void popScreen(int repeat) {
        for (int i = 0; i < repeat; i++) {
            gameScreenStack.pop();
        }

        game.setScreen(gameScreenStack.peek());
    }

    @Override
    public void dispose() {
        for (AbstractGameScreen screen : gameScreenStack) {
            screen.dispose();
        }
    }
}
