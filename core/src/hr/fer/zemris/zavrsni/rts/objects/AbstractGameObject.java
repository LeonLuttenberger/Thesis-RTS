package hr.fer.zemris.zavrsni.rts.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractGameObject {

    protected Vector2 position = new Vector2();
    protected Vector2 origin = new Vector2();
    protected Vector2 dimension = new Vector2(1, 1);
    protected Vector2 scale = new Vector2(1, 1);
    protected float rotation = 0f;

    protected TextureRegion textureRegion;

    public AbstractGameObject(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
        this.dimension.x = textureRegion.getRegionWidth();
        this.dimension.y = textureRegion.getRegionHeight();
    }

    public void render(SpriteBatch batch) {
        batch.draw(textureRegion.getTexture(), position.x, position.y, origin.x,
                origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
                textureRegion.getRegionX(), textureRegion.getRegionY(), textureRegion.getRegionWidth(),
                textureRegion.getRegionHeight(), false, false);
    }

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
