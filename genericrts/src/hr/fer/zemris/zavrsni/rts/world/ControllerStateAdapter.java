package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class ControllerStateAdapter implements IControllerState {

    @Override
    public void mouseMoved(int screenX, int screenY) {}

    @Override
    public void mouseLeftClicked(int screenX, int screenY) {}

    @Override
    public void mouseRightClicked(int screenX, int screenY) {}

    @Override
    public void mouseLeftDragged(int screenX, int screenY) {}

    @Override
    public void mouseRightDragged(int screenX, int screenY) {}

    @Override
    public void mouseLeftClickReleased(int screenX, int screenY) {}

    @Override
    public void mouseRightClickReleased(int screenX, int screenY) {}

    @Override
    public void keyDown(int keycode) {}

    @Override
    public void render(SpriteBatch batch) {}

    @Override
    public void resize(int width, int height) {}
}
