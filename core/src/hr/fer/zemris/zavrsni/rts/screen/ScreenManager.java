package hr.fer.zemris.zavrsni.rts.screen;

import com.sun.media.jfxmediaimpl.MediaDisposer.Disposable;
import hr.fer.zemris.zavrsni.rts.common.IGameSettings;
import hr.fer.zemris.zavrsni.rts.common.IGameState;

import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

public class ScreenManager implements Disposable {

    private final ScreenManagedGame game;
    private final IGameSettings gameSettings;
    private final Stack<AbstractGameScreen> gameScreenStack = new Stack<>();

    private MenuScreen menuScreen;
    private OptionsScreen optionsScreen;
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

    public void pushGameScreen(IGameState gameState) {
        pushScreen(new GameScreen(game, gameSettings, gameState));
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

    public Optional<GameScreen> getGameScreen() {
        List<GameScreen> gameScreens = gameScreenStack.stream()
                .filter(s -> s instanceof GameScreen)
                .map(s -> (GameScreen) s)
                .collect(Collectors.toList());

        if (gameScreens.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(gameScreens.get(gameScreens.size() - 1));
    }

    @Override
    public void dispose() {
        for (AbstractGameScreen screen : gameScreenStack) {
            screen.dispose();
        }
    }
}
