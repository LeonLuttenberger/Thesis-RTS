package hr.fer.zemris.zavrsni.rts.world.controllers.state;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IControllerState {

    void mouseMoved(int screenX, int screenY);

    void mouseLeftClicked(int screenX, int screenY);

    void mouseRightClicked(int screenX, int screenY);

    void mouseLeftDragged(int screenX, int screenY);

    void mouseRightDragged(int screenX, int screenY);

    void mouseLeftClickReleased(int screenX, int screenY);

    void mouseRightClickReleased(int screenX, int screenY);

    void keyDown(int keycode);

    void render(SpriteBatch batch);

    void resize(int width, int height);
}
