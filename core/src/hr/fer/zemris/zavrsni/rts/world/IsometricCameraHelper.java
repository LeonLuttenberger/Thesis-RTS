package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import hr.fer.zemris.zavrsni.rts.IUpdatable;

public class IsometricCameraHelper implements IUpdatable {

    private final float FOLLOW_SPEED = 4.0f;
    private final float MAX_ZOOM_IN = 0.25f;
    private final float MAX_ZOOM_OUT = 10.0f;

    private Vector3 position;
    private Vector3 direction;
    private float near;
    private float far;
    private float zoom;

    private final Matrix4 matrix;
    private final Plane xzPlane;

    public IsometricCameraHelper() {
        position = new Vector3(5, 5, 10);
        direction = new Vector3(-1, -1, -1);
        near = 1;
        far = 100;
        zoom = 1f;

        matrix = new Matrix4();
        matrix.setToRotation(new Vector3(1, 0, 0), 90);

        xzPlane = new Plane(new Vector3(0, 1, 0), 0);
    }

    @Override
    public void update(float deltaTime) {

    }

    public Vector3 getSelectedPoint(OrthographicCamera camera, float x, float y) {
        Vector3 intersection = new Vector3();

        Ray pickRay = camera.getPickRay(x, y);
        Intersector.intersectRayPlane(pickRay, xzPlane, intersection);

        return intersection;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(float x, float z) {
        position.x = x;
        position.z = z;
    }

    public Plane getXzPlane() {
        return xzPlane;
    }

    public Matrix4 getMatrix() {
        return matrix;
    }

    public boolean hasTarget() {
        return false;
    }

    public void addZoom(float zoomDelta) {
        setZoom(zoom + zoomDelta);
    }

    public void setZoom(float zoom) {
        this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
    }

    public void applyTo(OrthographicCamera camera) {
        camera.position.set(position);
        camera.direction.set(direction);

        camera.near = near;
        camera.far = far;
        camera.zoom = zoom;

        camera.update();
    }
}
