package hr.fer.zemris.zavrsni.rts.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import hr.fer.zemris.zavrsni.rts.world.IsometricCameraHelper;
import hr.fer.zemris.zavrsni.rts.world.WorldController;
import hr.fer.zemris.zavrsni.rts.world.WorldRenderer;

public class GameScreen extends AbstractGameScreen {

    private static final String TAG = GameScreen.class.getName();

    private WorldController controller;
    private WorldRenderer renderer;

    private boolean paused;
    private boolean justUnpaused;

    public GameScreen(Game game) {
        super(game);
        setInputProcessor(new GameScreenInputProcessor());

        controller = new WorldController(game);
        renderer = new WorldRenderer(controller);
    }

    @Override
    public void render(float delta) {
        handleInput(delta);

        controller.update(delta);

        // Sets the clear screen color to: Cornflower Blue
        Gdx.gl20.glClearColor(
                0x64/255.0f,
                0x95/255.0f,
                0xed/255.0f,
                0xff/255.0f
        );

        // Clears the screen
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    private void handleInput(float deltaTime) {
        IsometricCameraHelper cameraHelper = controller.getCameraHelper();

        if (!cameraHelper.hasTarget()) {

            // Camera Controls (move)
            float camMoveSpeed = 5 * deltaTime;
            float camMoveSpeedAccelerationFactor = 5;

            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                camMoveSpeed *= camMoveSpeedAccelerationFactor;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                moveCamera(camMoveSpeed, -camMoveSpeed);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                moveCamera(-camMoveSpeed, camMoveSpeed);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                moveCamera(camMoveSpeed, camMoveSpeed);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                moveCamera(-camMoveSpeed, -camMoveSpeed);
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
        IsometricCameraHelper cameraHelper = controller.getCameraHelper();

        float positionX = cameraHelper.getPosition().x;
        float positionZ = cameraHelper.getPosition().z;

        cameraHelper.setPosition(positionX + x, positionZ + z);
    }

    private class GameScreenInputProcessor extends InputAdapter {

        final Vector3 last = new Vector3(-1, -1, -1);

        @Override
        public boolean touchDragged(int x, int y, int pointer) {
            IsometricCameraHelper cameraHelper = controller.getCameraHelper();
            OrthographicCamera camera = renderer.getCamera();

            Vector3 curr = cameraHelper.getSelectedPoint(camera, x, y);

            if(!(last.x == -1 && last.y == -1 && last.z == -1)) {
                Vector3 delta = cameraHelper.getSelectedPoint(camera, last.x, last.y);
                delta.sub(curr);

                cameraHelper.getPosition().add(delta.x, delta.y, delta.z);
            }

            last.set(x, y, 0);
            return false;
        }

        @Override public boolean touchUp(int x, int y, int pointer, int button) {
            last.set(-1, -1, -1);
            return false;
        }
    }
}
