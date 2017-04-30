package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.Game;
import hr.fer.zemris.zavrsni.rts.objects.units.AbstractUnit;

public class WorldController implements IWorldController {

    private static final String TAG = WorldController.class.getName();

    private final Game game;
    private final GameState gameState;
    private final PathFindingController pathFindingController;

    public WorldController(Game game, Level level) {
        this.game = game;

        this.gameState = new GameState();
        this.gameState.setLevel(level);

        pathFindingController = new PathFindingController(level);
    }

    public GameState getGameState() {
        return gameState;
    }

    public PathFindingController getPathFindingController() {
        return pathFindingController;
    }

    @Override
    public void update(float deltaTime) {
        pathFindingController.update(deltaTime);

        for (AbstractUnit unit : gameState.getLevel().getUnits()) {
            unit.update(deltaTime);
        }
    }

    @Override
    public void dispose() {
    }
}
