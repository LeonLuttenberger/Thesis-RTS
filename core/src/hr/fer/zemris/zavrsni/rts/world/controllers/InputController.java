package hr.fer.zemris.zavrsni.rts.world.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.buildings.GeneratorBuilding;
import hr.fer.zemris.zavrsni.rts.world.ILevel;
import hr.fer.zemris.zavrsni.rts.world.renderers.DragBoxRenderer;

import java.util.function.Consumer;

public class InputController extends InputAdapter {

    private DragBoxRenderer dragBoxRenderer;
    private OrthographicCamera camera;
    private IWorldController controller;

    private Consumer<Building> onBuildingSelected;

    private Building newBuilding;

    public InputController(DragBoxRenderer dragBoxRenderer, OrthographicCamera camera, IWorldController controller) {
        this.dragBoxRenderer = dragBoxRenderer;
        this.camera = camera;
        this.controller = controller;
    }

    public void setOnBuildingSelected(Consumer<Building> onBuildingSelected) {
        this.onBuildingSelected = onBuildingSelected;
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

    public Building getNewBuilding() {
        return newBuilding;
    }

    private final Vector3 dragLast = new Vector3(-1, -1, -1);

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            dragBoxRenderer.handleTouchDrag(screenX, screenY);
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
        if (button == Input.Buttons.LEFT) {
            dragBoxRenderer.handleTouchUp();
            handleUnitSelection();
        }

        if (button == Buttons.MIDDLE) {
            dragLast.set(-1, -1, -1);
        }

        return false;
    }

    private void handleUnitSelection() {
        Vector3 selectionStart = camera.unproject(new Vector3(dragBoxRenderer.getX(), dragBoxRenderer.getY(), 0));
        Vector3 selectionEnd = camera.unproject(new Vector3(
                dragBoxRenderer.getX() + dragBoxRenderer.getWidth(),
                dragBoxRenderer.getY() + dragBoxRenderer.getHeight(),
                0));

        controller.selectUnitsInArea(selectionStart, selectionEnd);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            if (newBuilding != null) {
                controller.getGameState().getLevel().addBuilding(newBuilding);
                newBuilding = null;
            } else {
                // select existing building
                Vector3 position = camera.unproject(new Vector3(screenX, screenY, 0));
                for (Building building : controller.getGameState().getLevel().getBuildings()) {
                    if (building.containsPoint(position.x, position.y)) {
                        if (onBuildingSelected != null) {
                            onBuildingSelected.accept(building);
                        }
                        break;
                    }
                }
            }

        } else if (button == Input.Buttons.RIGHT) {
            Vector3 position = camera.unproject(new Vector3(screenX, screenY, 0));
            controller.sendSelectedUnitsTo(position);
        }

        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.B) {
            newBuilding = new GeneratorBuilding(controller.getGameState().getLevel()); //TODO
            mouseMoved(Gdx.input.getX(), Gdx.input.getY());
        } else if (keycode == Keys.ESCAPE) {
            newBuilding = null;
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (newBuilding != null) {
            Vector3 position = camera.unproject(new Vector3(screenX, screenY, 0));

            ILevel level = controller.getGameState().getLevel();
            position.set(
                    position.x - position.x % level.getTileWidth() + level.getTileWidth() / 2,
                    position.y - position.y % level.getTileHeight() + level.getTileHeight() / 2,
                    0
            );

            newBuilding.setCenterX(position.x);
            newBuilding.setCenterY(position.y);
        }

        return false;
    }
}
