package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Disposable;
import hr.fer.zemris.zavrsni.rts.IUpdatable;

public class WorldController implements Disposable, IUpdatable {

    private static final String TAG = WorldController.class.getName();

    private final Game game;
    private final GameState gameState;

    public WorldController(Game game, Level level) {
        this.game = game;

        this.gameState = new GameState();
        this.gameState.setLevel(level);
    }

    public GameState getGameState() {
        return gameState;
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void dispose() {
    }
}
