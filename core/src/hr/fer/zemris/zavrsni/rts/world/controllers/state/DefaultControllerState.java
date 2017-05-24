package hr.fer.zemris.zavrsni.rts.world.controllers.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import hr.fer.zemris.zavrsni.rts.common.IGameState;
import hr.fer.zemris.zavrsni.rts.objects.units.PlayerUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.squad.ISquad;
import hr.fer.zemris.zavrsni.rts.world.ControllerStateAdapter;
import hr.fer.zemris.zavrsni.rts.world.renderers.DragBoxRenderer;

public class DefaultControllerState extends ControllerStateAdapter {

    private final OrthographicCamera camera;
    private final IGameState gameState;
    private final DragBoxRenderer dragBoxRenderer = new DragBoxRenderer();

    public DefaultControllerState(OrthographicCamera camera, IGameState gameState) {
        this.camera = camera;
        this.gameState = gameState;
    }

    @Override
    public void mouseLeftClicked(int screenX, int screenY) {
        // deselect units
        for (PlayerUnit playerUnit : gameState.getLevel().getPlayerUnits()) {
            playerUnit.setSelected(false);
        }
    }

    @Override
    public void mouseRightClicked(int screenX, int screenY) {
        Vector3 position = camera.unproject(new Vector3(screenX, screenY, 0));

        ISquad squad = gameState.createSquadFromSelected();
        if (squad != null) {
            squad.sendToLocation(position.x, position.y);
        }
    }

    @Override
    public void mouseLeftClickReleased(int screenX, int screenY) {
        dragBoxRenderer.handleTouchUp();
        handleUnitSelection();
    }

    private void handleUnitSelection() {
        Vector3 areaStart = camera.unproject(new Vector3(dragBoxRenderer.getX(), dragBoxRenderer.getY(), 0));
        Vector3 areaEnd = camera.unproject(new Vector3(
                dragBoxRenderer.getX() + dragBoxRenderer.getWidth(),
                dragBoxRenderer.getY() + dragBoxRenderer.getHeight(),
                0));

        Rectangle selectionArea = new Rectangle(
                Math.min(areaStart.x, areaEnd.x),
                Math.min(areaStart.y, areaEnd.y),
                Math.abs(areaEnd.x - areaStart.x),
                Math.abs(areaEnd.y - areaStart.y)
        );

        for (PlayerUnit unit : gameState.getLevel().getPlayerUnits()) {
            if (selectionArea.contains(unit.getCenterX(), unit.getCenterY())) {
                unit.setSelected(true);
            } else {
                unit.setSelected(false);
            }
        }
    }

    @Override
    public void mouseLeftDragged(int screenX, int screenY) {
        dragBoxRenderer.handleTouchDrag(screenX, screenY);
    }

    @Override
    public void keyDown(int keycode) {
        if (keycode >= Keys.NUM_0 && keycode <= Keys.NUM_9) {
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
