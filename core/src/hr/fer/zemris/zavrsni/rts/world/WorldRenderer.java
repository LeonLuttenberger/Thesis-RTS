package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import hr.fer.zemris.zavrsni.rts.util.Constants;

public class WorldRenderer implements Disposable {

    private SpriteBatch batch;
    private WorldController controller;

    private OrthographicCamera camera;
    private OrthographicCamera cameraGUI;

    public WorldRenderer(WorldController controller) {
        this.controller = controller;

        batch = new SpriteBatch();

        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();

        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(true);
        cameraGUI.update();

        batch = new SpriteBatch();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderWorld();
    }

    public void resize(int width, int height) {
        camera.viewportWidth = Constants.VIEWPORT_HEIGHT * width / height;
        camera.viewportHeight = Constants.VIEWPORT_HEIGHT;
        camera.update();
    }

    private void renderWorld() {
        controller.getCameraHelper().applyTo(camera);

        batch.setProjectionMatrix(camera.combined);
        batch.setTransformMatrix(controller.getCameraHelper().getMatrix());

        batch.begin();
        controller.getGameState().getLevel().render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
