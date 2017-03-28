package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import hr.fer.zemris.zavrsni.rts.IUpdatable;

public class WorldController implements Disposable, IUpdatable {

    private static final String TAG = WorldController.class.getName();

    private Game game;
    private GameState gameState;
    private IsometricCameraHelper cameraHelper;

//    private World b2world;

    public WorldController(Game game) {
        this.game = game;
        this.cameraHelper = new IsometricCameraHelper();
//        this.b2world = new World(new Vector2(0, 9.81f), false);
    }

    public IsometricCameraHelper getCameraHelper() {
        return cameraHelper;
    }

    public GameState getGameState() {
        return gameState;
    }

//    public World getB2world() {
//        return b2world;
//    }

    @Override
    public void update(float deltaTime) {
        handleDebugInput(deltaTime);

        cameraHelper.update(deltaTime);
//        b2world.step(deltaTime, 8, 3);
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
//        b2world.dispose();
    }
}
