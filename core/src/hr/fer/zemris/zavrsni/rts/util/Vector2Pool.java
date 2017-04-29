package hr.fer.zemris.zavrsni.rts.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Vector2Pool extends Pool<Vector2> {

    private static final int POOL_CAPACITY = 16;

    private static final Vector2Pool INSTANCE = new Vector2Pool();

    public static Vector2Pool getInstance() {
        return INSTANCE;
    }

    private Vector2Pool() {
        super(POOL_CAPACITY);
    }

    @Override
    protected Vector2 newObject() {
        return new Vector2();
    }
}
