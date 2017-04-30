package hr.fer.zemris.zavrsni.rts.objects.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import hr.fer.zemris.zavrsni.rts.objects.AbstractMovableObject;

import java.util.LinkedList;
import java.util.Queue;

public abstract class AbstractUnit extends AbstractMovableObject {

    private static final int TOLERANCE = 10;

    protected final Animation<TextureRegion> animation;

    protected boolean isSelected;
    protected final Queue<Vector2> waypoints = new LinkedList<>();

    private float stateTime;

    public AbstractUnit(Animation<TextureRegion> animation, float width, float height) {
        this.animation = animation;

        this.dimension.x = width;
        this.dimension.y = height;
    }

    @Override
    public abstract float getMaxSpeed();

    @Override
    public void render(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();

        if (!isMoving()) {
            TextureRegion frame = animation.getKeyFrame(0);
            batch.draw(frame.getTexture(), position.x, position.y, origin.x,
                    origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
                    frame.getRegionX(), frame.getRegionY(), frame.getRegionWidth(),
                    frame.getRegionHeight(), false, false);
            return;
        }

        TextureRegion frame = animation.getKeyFrame(stateTime);
        boolean flipX = velocity.x < 0;

        batch.draw(frame.getTexture(), position.x, position.y, origin.x, origin.y,
                dimension.x, dimension.y, scale.x, scale.y, rotation,
                frame.getRegionX(), frame.getRegionY(), frame.getRegionWidth(),
                frame.getRegionHeight(), flipX, false);
    }

    @Override
    public void update(float deltaTime) {
        if (waypoints.isEmpty()) {
            velocity.set(0, 0);
            return;
        }

        super.update(deltaTime);

        Vector2 waypoint = waypoints.peek();

        if (Math.abs(waypoint.x - getCenterX()) < TOLERANCE && Math.abs(waypoint.y - getCenterY()) < TOLERANCE) {
            waypoints.poll();
        }
    }

    public void moveTowardsWaypoint(float speed) {
        Vector2 waypoint = waypoints.peek();

        velocity.x = waypoint.x - getCenterX();
        velocity.y = waypoint.y - getCenterY();
        velocity.setLength(speed);
    }

    public void addWaypoint(Vector2 waypoint) {
        waypoints.add(waypoint);
    }

    public void clearWaypoints() {
        waypoints.clear();
    }

    public boolean hasWaypoint() {
        return !waypoints.isEmpty();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
