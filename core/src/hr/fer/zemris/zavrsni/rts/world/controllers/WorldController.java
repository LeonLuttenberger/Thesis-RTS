package hr.fer.zemris.zavrsni.rts.world.controllers;

import com.badlogic.gdx.Game;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;
import hr.fer.zemris.zavrsni.rts.world.GameState;
import hr.fer.zemris.zavrsni.rts.world.IGameState;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public class WorldController implements IWorldController {

    private static final String TAG = WorldController.class.getName();

    private final Game game;
    private final IGameState gameState;
    private final PathFindingController pathFindingController;

    public WorldController(Game game, ILevel level) {
        this.game = game;

        this.gameState = new GameState();
        this.gameState.setLevel(level);

        pathFindingController = new PathFindingController(level);
    }

    public IGameState getGameState() {
        return gameState;
    }

    public PathFindingController getPathFindingController() {
        return pathFindingController;
    }

    @Override
    public void update(float deltaTime) {
        pathFindingController.update(deltaTime);

        for (Unit unit : gameState.getLevel().getUnits()) {
            unit.update(deltaTime);
        }
    }

    @Override
    public void dispose() {
    }
}
