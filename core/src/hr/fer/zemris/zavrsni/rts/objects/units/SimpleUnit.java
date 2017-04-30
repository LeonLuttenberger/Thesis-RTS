package hr.fer.zemris.zavrsni.rts.objects.units;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.objects.AbstractMovableObject;

import java.util.LinkedList;
import java.util.Queue;

public class SimpleUnit extends AbstractMovableObject {

    public static final float DEFAULT_SPEED = 100 * 5;

    private static final int TOLERANCE = 10;

    private boolean isSelected;
    private Queue<Vector2> waypoints = new LinkedList<>();

    public SimpleUnit() {
        super(Assets.getInstance().getUnits().simpleUnit);
    }

    @Override
    public float getMaxSpeed() {
        return DEFAULT_SPEED;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (isSelected) {
            Color old = batch.getColor();
            batch.setColor(Color.RED);

            super.render(batch);

            batch.setColor(old);
        } else {
            super.render(batch);
        }
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
