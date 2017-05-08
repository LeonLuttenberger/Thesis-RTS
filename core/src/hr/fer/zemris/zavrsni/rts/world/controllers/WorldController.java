package hr.fer.zemris.zavrsni.rts.world.controllers;

import com.badlogic.gdx.Game;
import hr.fer.zemris.zavrsni.rts.world.GameState;
import hr.fer.zemris.zavrsni.rts.world.IGameState;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public class WorldController implements IWorldController {

    private final Game game;
    private final IGameState gameState;

    private boolean isPaused = false;
    private boolean wasPausedLastUpdate = false;

    public WorldController(Game game, ILevel level) {
        this.game = game;

        this.gameState = new GameState();
        this.gameState.setLevel(level);
    }

    public IGameState getGameState() {
        return gameState;
    }

    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
    public void resume() {
        isPaused = false;
    }

    @Override
    public void update(float deltaTime) {
        deltaTime = wasPausedLastUpdate ? 0 : deltaTime;

        if (!isPaused) {
            gameState.getLevel().update(deltaTime);
            wasPausedLastUpdate = false;
        } else {
            wasPausedLastUpdate = true;
        }

    }

    @Override
    public void dispose() {
    }
}
