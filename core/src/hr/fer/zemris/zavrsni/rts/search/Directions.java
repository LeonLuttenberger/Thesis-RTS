package hr.fer.zemris.zavrsni.rts.search;

import com.badlogic.gdx.math.Vector2;

public enum Directions {

    NORTH (new Vector2(0, 1)),
    SOUTH (new Vector2(0, -1)),
    EAST (new Vector2(1, 0)),
    WEST (new Vector2(-1, 0)),

    NORTH_EAST (new Vector2(1, 1).nor()),
    NORTH_WEST (new Vector2(-1, 1).nor()),
    SOUTH_EAST (new Vector2(1, -1).nor()),
    SOUTH_WEST (new Vector2(-1, -1).nor());

    private final Vector2 direction;

    Directions(Vector2 direction) {
        this.direction = direction;
    }

    public void apply(Vector2 vector) {
        vector.set(direction);
    }

    public float getX() {
        return direction.x;
    }

    public float getY() {
        return direction.y;
    }
}
