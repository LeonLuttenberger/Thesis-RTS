package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
        handleDebugInput(deltaTime);
        cameraHelper.update(deltaTime);
    }

    private void handleDebugInput(float deltaTime) {
        if (!cameraHelper.hasTarget()) {

            // Camera Controls (move)
            float camMoveSpeed = 5 * deltaTime;
            float camMoveSpeedAccelerationFactor = 5;

            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                camMoveSpeed *= camMoveSpeedAccelerationFactor;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                moveCamera(-camMoveSpeed, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                moveCamera(camMoveSpeed, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                moveCamera(0, camMoveSpeed);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                moveCamera(0, -camMoveSpeed);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) {
                cameraHelper.setPosition(0, 0);
            }
        }

        // Camera Controls (zoom)
        float camZoomSpeed = 1 * deltaTime;
        float camZoomSpeedAccelerationFactor = 5;

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            camZoomSpeed *= camZoomSpeedAccelerationFactor;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.COMMA)) {
            cameraHelper.addZoom(camZoomSpeed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) {
            cameraHelper.addZoom(-camZoomSpeed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SLASH)) {
            cameraHelper.setZoom(1);
        }
    }

    private void moveCamera(float x, float z) {
        float positionX = cameraHelper.getPosition().x;
        float positionZ = cameraHelper.getPosition().z;

        cameraHelper.setPosition(positionX + x, positionZ + z);
    }

    @Override
    public void dispose() {
    }
}
