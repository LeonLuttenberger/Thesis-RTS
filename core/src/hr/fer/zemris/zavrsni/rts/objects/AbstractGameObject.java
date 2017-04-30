package hr.fer.zemris.zavrsni.rts.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractGameObject {

    protected final Vector2 position = new Vector2();
    protected final Vector2 origin = new Vector2();
    protected final Vector2 dimension = new Vector2(1, 1);
    protected final Vector2 scale = new Vector2(1, 1);
    protected float rotation = 0f;

    private float stateTime = 0;

    protected final Animation<TextureRegion> animation;

    public AbstractGameObject(Animation<TextureRegion> animation) {
        this.animation = animation;

        TextureRegion textureRegion = animation.getKeyFrame(0);
        this.dimension.x = textureRegion.getRegionWidth();
        this.dimension.y = textureRegion.getRegionHeight();
    }

    public AbstractGameObject(Animation<TextureRegion> animation, float width, float height) {
        this.animation = animation;

        this.dimension.x = width;
        this.dimension.y = height;
    }

    public void render(SpriteBatch batch) {
        TextureRegion frame = getCurrentFrame();

        batch.draw(frame.getTexture(), position.x, position.y, origin.x,
                origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
                frame.getRegionX(), frame.getRegionY(), frame.getRegionWidth(),
                frame.getRegionHeight(), false, false);
    }

    protected final TextureRegion getCurrentFrame() {
        stateTime += Gdx.graphics.getDeltaTime();
        return animation.getKeyFrame(stateTime);
    }

    protected final void renderWithTexture(SpriteBatch batch, TextureRegion frame) {
        batch.draw(frame.getTexture(), position.x, position.y, origin.x,
                origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
                frame.getRegionX(), frame.getRegionY(), frame.getRegionWidth(),
                frame.getRegionHeight(), false, false);
    }

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
}
