package hr.fer.zemris.zavrsni.rts.screen;

import com.sun.media.jfxmediaimpl.MediaDisposer.Disposable;
import hr.fer.zemris.zavrsni.rts.common.IGameSettings;

import java.util.Stack;

public class ScreenManager implements Disposable {

    private final ScreenManagedGame game;
    private final IGameSettings gameSettings;
    private final Stack<AbstractGameScreen> gameScreenStack = new Stack<>();

    private MenuScreen menuScreen;
    private OptionsScreen optionsScreen;
    private GameScreen gameScreen;
    private PauseScreen pauseScreen;

    public ScreenManager(ScreenManagedGame game, IGameSettings gameSettings) {
        this.game = game;
        this.gameSettings = gameSettings;
    }

    public void pushScreen(AbstractGameScreen gameScreen) {
        gameScreenStack.push(gameScreen);

        game.setScreen(gameScreen);
    }

    public void pushMenuScreen() {
        if (menuScreen == null) {
            menuScreen = new MenuScreen(game, gameSettings);
        }

        pushScreen(menuScreen);
    }

    public void pushOptionsScreen() {
        if (optionsScreen == null) {
            optionsScreen = new OptionsScreen(game, gameSettings);
        }

        pushScreen(optionsScreen);
    }

    public void pushGameScreen() {
        pushScreen(new GameScreen(game, gameSettings));
    }

    public void pushPauseScreen() {
        if (pauseScreen == null) {
            pauseScreen = new PauseScreen(game, gameSettings);
        }

        pushScreen(pauseScreen);
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
