package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import hr.fer.zemris.zavrsni.rts.objects.units.SimpleUnit;
import hr.fer.zemris.zavrsni.rts.screen.DragBoxRenderer;

public class InteractionController extends InputAdapter {

    private DragBoxRenderer dragBoxRenderer;
    private OrthographicCamera camera;
    private WorldController controller;

    public InteractionController(DragBoxRenderer dragBoxRenderer, OrthographicCamera camera, WorldController controller) {
        this.dragBoxRenderer = dragBoxRenderer;
        this.camera = camera;
        this.controller = controller;
    }

    public void handleInput(float deltaTime) {
        handleCameraControls(deltaTime);
    }

    private void handleCameraControls(float deltaTime) {
        // Camera Controls (move)
        float camMoveSpeed = 100 * deltaTime;
        float camMoveSpeedAccelerationFactor = 5;

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            camMoveSpeed *= camMoveSpeedAccelerationFactor;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            translateCamera(-camMoveSpeed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            translateCamera(camMoveSpeed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            translateCamera(0, camMoveSpeed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            translateCamera(0, -camMoveSpeed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) {
            camera.position.set(0, 0, camera.position.z);
        }

        // Camera Controls (zoom)
        float camZoomSpeed = 1 * deltaTime;
        float camZoomSpeedAccelerationFactor = 5;

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            camZoomSpeed *= camZoomSpeedAccelerationFactor;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.COMMA)) {
            camera.zoom += camZoomSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) {
            camera.zoom -= camZoomSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SLASH)) {
            camera.zoom = 1f;
        }
    }

    private void translateCamera(float x, float y) {
        camera.translate(x, y);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            dragBoxRenderer.handleTouchDrag(screenX, screenY);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            dragBoxRenderer.handleTouchUp();
            handleUnitSelection();
        }
        return false;
    }

    private void handleUnitSelection() {
        Vector3 selectionStart = camera.unproject(new Vector3(dragBoxRenderer.getX(), dragBoxRenderer.getY(), 0));
        Vector3 selectionEnd = camera.unproject(new Vector3(
                dragBoxRenderer.getX() + dragBoxRenderer.getWidth(),
                dragBoxRenderer.getY() + dragBoxRenderer.getHeight(),
                0));

        controller.getGameState().selectUnitsInArea(selectionStart, selectionEnd);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.RIGHT) return false;

        Vector3 position = camera.unproject(new Vector3(screenX, screenY, 0));
        System.out.println(position);

        for (SimpleUnit unit : controller.getGameState().getSelectedUnits()) {
            unit.setCenterX(position.x);
            unit.setCenterY(position.y);
        }

        return false;
    }
}
