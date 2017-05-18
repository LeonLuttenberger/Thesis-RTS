package hr.fer.zemris.zavrsni.rts.world.controllers.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import hr.fer.zemris.zavrsni.rts.common.IGameState;
import hr.fer.zemris.zavrsni.rts.world.controllers.WorldController;
import hr.fer.zemris.zavrsni.rts.world.renderers.DragBoxRenderer;

public class DefaultControllerState extends ControllerStateAdapter {

    private final WorldController controller;
    private final DragBoxRenderer dragBoxRenderer = new DragBoxRenderer();

    public DefaultControllerState(WorldController controller) {
        this.controller = controller;
    }

    @Override
    public void mouseLeftClicked(int screenX, int screenY) {
        controller.deselectUnits();
    }

    @Override
    public void mouseRightClicked(int screenX, int screenY) {
        Vector3 position = controller.getCamera().unproject(new Vector3(screenX, screenY, 0));
        controller.sendSelectedUnitsTo(position);
    }

    @Override
    public void mouseLeftClickReleased(int screenX, int screenY) {
        dragBoxRenderer.handleTouchUp();
        handleUnitSelection();
    }

    private void handleUnitSelection() {
        OrthographicCamera camera = controller.getCamera();

        Vector3 selectionStart = camera.unproject(new Vector3(dragBoxRenderer.getX(), dragBoxRenderer.getY(), 0));
        Vector3 selectionEnd = camera.unproject(new Vector3(
                dragBoxRenderer.getX() + dragBoxRenderer.getWidth(),
                dragBoxRenderer.getY() + dragBoxRenderer.getHeight(),
                0));

        controller.selectUnitsInArea(selectionStart, selectionEnd);
    }

    @Override
    public void mouseLeftDragged(int screenX, int screenY) {
        dragBoxRenderer.handleTouchDrag(screenX, screenY);
    }

    @Override
    public void keyDown(int keycode) {
        if (keycode >= Keys.NUM_0 && keycode <= Keys.NUM_9) {
            IGameState gameState = controller.getGameState();

            if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
                gameState.saveCurrentSelection(Keys.toString(keycode));
            } else {
                gameState.selectSavedSelection(Keys.toString(keycode));
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.end();
        dragBoxRenderer.render();
        batch.begin();
    }

    @Override
    public void resize(int width, int height) {
        dragBoxRenderer.resize(width, height);
    }
}
