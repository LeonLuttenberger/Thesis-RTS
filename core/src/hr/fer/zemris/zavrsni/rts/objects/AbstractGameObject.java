package hr.fer.zemris.zavrsni.rts.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractGameObject {

    protected Vector2 position = new Vector2();
    protected Vector2 origin = new Vector2();
    protected Vector2 dimension = new Vector2(1, 1);
    protected Vector2 scale = new Vector2(1, 1);
    protected float rotation = 0f;

    public abstract void render(SpriteBatch batch);

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getOrigin() {
        return origin;
    }

    public Vector2 getDimension() {
        return dimension;
    }

    public Vector2 getScale() {
        return scale;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
