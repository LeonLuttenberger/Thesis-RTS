package hr.fer.zemris.zavrsni.rts.world.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class InputController extends InputAdapter {

    private OrthographicCamera camera;
    private WorldController controller;

    public InputController(OrthographicCamera camera, WorldController controller) {
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

    private final Vector3 dragLast = new Vector3(-1, -1, -1);

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
            controller.getControllerState().mouseLeftDragged(screenX, screenY);
        }

        if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
            controller.getControllerState().mouseRightDragged(screenX, screenY);
        }

        if (Gdx.input.isButtonPressed(Buttons.MIDDLE)) {
            if (dragLast.x != -1 || dragLast.y != -1 || dragLast.z != -1) {
                translateCamera(dragLast.x - screenX, screenY - dragLast.y);
            }
        }

        dragLast.set(screenX, screenY, 0);

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Buttons.LEFT) {
            controller.getControllerState().mouseLeftClickReleased(screenX, screenY);
        }

        if (button == Buttons.RIGHT) {
            controller.getControllerState().mouseRightClickReleased(screenX, screenY);
        }

        if (button == Buttons.MIDDLE) {
            dragLast.set(-1, -1, -1);
        }

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Buttons.RIGHT) {
            controller.getControllerState().mouseRightClicked(screenX, screenY);
        }

        if (button == Buttons.LEFT) {
            controller.getControllerState().mouseLeftClicked(screenX, screenY);
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        controller.getControllerState().mouseMoved(screenX, screenY);
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        controller.getControllerState().keyDown(keycode);
        return false;
    }
}
