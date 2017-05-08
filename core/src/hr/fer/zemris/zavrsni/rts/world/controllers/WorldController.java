package hr.fer.zemris.zavrsni.rts.world.controllers;

import com.badlogic.gdx.Game;
import hr.fer.zemris.zavrsni.rts.objects.resources.Resource;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;
import hr.fer.zemris.zavrsni.rts.world.GameState;
import hr.fer.zemris.zavrsni.rts.world.IGameState;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

import java.util.ArrayList;
import java.util.List;

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
        if (isPaused) {
            wasPausedLastUpdate = true;
            return;
        }

        deltaTime = wasPausedLastUpdate ? 0 : deltaTime;
        wasPausedLastUpdate = false;

        ILevel level = gameState.getLevel();
        for (Unit unit : level.getUnits()) {
            unit.update(deltaTime);
        }

        List<Resource> resourcesToRemove = new ArrayList<>();
        for (Resource resource : level.getResources()) {
            if (resource.getRemainingDurability() <= 0) {
                resource.onResourceDestroyed(gameState);
                resourcesToRemove.add(resource);
            }
        }
        for (Resource resource : resourcesToRemove) {
            level.removeResource(resource);
        }
    }

    @Override
    public void dispose() {
    }
}
