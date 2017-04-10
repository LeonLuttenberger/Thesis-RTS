package hr.fer.zemris.zavrsni.rts.objects;

import com.badlogic.gdx.math.Vector2;
import hr.fer.zemris.zavrsni.rts.IUpdatable;

public abstract class AbstractMovableObject extends AbstractGameObject implements IUpdatable {

    private Vector2 velocity;

    public Vector2 getVelocity() {
        return velocity;
    }

    @Override
    public void update(float deltaTime) {
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
    }
}
