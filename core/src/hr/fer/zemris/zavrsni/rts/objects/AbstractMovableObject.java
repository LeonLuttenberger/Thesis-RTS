package hr.fer.zemris.zavrsni.rts.objects;

import com.badlogic.gdx.math.Vector2;
import hr.fer.zemris.zavrsni.rts.IUpdatable;

public abstract class AbstractMovableObject extends AbstractGameObject implements IUpdatable {

    protected Vector2 velocity = new Vector2();

    @Override
    public void update(float deltaTime) {
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getSpeed() {
        return velocity.len();
    }

    public void setSpeed(float speed) {
        velocity.setLength(speed);
    }

    public boolean isMoving() {
        return !velocity.isZero();
    }
}
