package hr.fer.zemris.zavrsni.rts.objects;

import com.badlogic.gdx.math.Vector2;
import hr.fer.zemris.zavrsni.rts.common.IUpdateable;

public abstract class AbstractMovableObject extends AbstractGameObject implements IUpdateable {

    public final Vector2 velocity = new Vector2();

    @Override
    public void update(float deltaTime) {
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
    }

    public final boolean isMoving() {
        return !velocity.isZero();
    }
}
