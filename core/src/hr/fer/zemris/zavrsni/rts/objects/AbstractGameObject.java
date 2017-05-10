package hr.fer.zemris.zavrsni.rts.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractGameObject {

    protected final Vector2 position = new Vector2();
    protected final Vector2 origin = new Vector2();
    protected final Vector2 dimension = new Vector2(1, 1);
    protected final Vector2 scale = new Vector2(1, 1);
    protected float rotation = 0f;

    public abstract void render(SpriteBatch batch);

    public final Vector2 getPosition() {
        return position;
    }

    public final Vector2 getOrigin() {
        return origin;
    }

    public final Vector2 getDimension() {
        return dimension;
    }

    public final Vector2 getScale() {
        return scale;
    }

    public final float getRotation() {
        return rotation;
    }

    public final void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public final float getCenterX() {
        return position.x + dimension.x / 2;
    }

    public final void setCenterX(float x) {
        position.x = x - dimension.x / 2;
    }

    public final float getCenterY() {
        return position.y + dimension.y / 2;
    }

    public final void setCenterY(float y) {
        position.y = y - dimension.y / 2;
    }

    public static float distanceBetween(AbstractGameObject o1, AbstractGameObject o2) {
        return distanceBetween(o1.getCenterX(), o1.getCenterY(), o2.getCenterX(), o2.getCenterY());
    }

    public static float distanceBetween(AbstractGameObject o1, float pointX, float pointY) {
        return distanceBetween(o1.getCenterX(), o1.getCenterY(), pointX, pointY);
    }

    private static float distanceBetween(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
