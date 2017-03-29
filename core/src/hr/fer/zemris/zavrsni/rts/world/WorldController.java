package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Disposable;
import hr.fer.zemris.zavrsni.rts.IUpdatable;

public class WorldController implements Disposable, IUpdatable {

    private static final String TAG = WorldController.class.getName();

    private final Game game;
    private final GameState gameState;
    private final IsometricCameraHelper cameraHelper;

    public WorldController(Game game) {
        this.game = game;
        this.cameraHelper = new IsometricCameraHelper();

        this.gameState = new GameState();
        this.gameState.setLevel(new Level());
    }

    public IsometricCameraHelper getCameraHelper() {
        return cameraHelper;
    }

    public GameState getGameState() {
        return gameState;
    }

    @Override
    public void update(float deltaTime) {
        cameraHelper.update(deltaTime);
    }

    @Override
    public void dispose() {
    }
}
