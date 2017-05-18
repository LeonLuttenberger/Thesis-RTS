package hr.fer.zemris.zavrsni.rts.world.controllers.state;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IControllerState {

    void mouseMoved(int screenX, int screenY);

    void mouseClicked(int screenX, int screenY);

    void render(SpriteBatch batch);
}
