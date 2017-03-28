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

    private static final boolean DEBUG_DRAW_BOX2D_WORLD = false;

    private Box2DDebugRenderer b2debugRenderer;

    private SpriteBatch batch;
    private WorldController controller;

    private OrthographicCamera camera;
    private OrthographicCamera cameraGUI;

    private Texture texture;
    final Sprite[][] sprites = new Sprite[10][10];

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

        b2debugRenderer = new Box2DDebugRenderer();

        // TODO delete this
        texture = new Texture(Gdx.files.internal("images/grass.png"));

        for(int z = 0; z < 10; z++) {
            for(int x = 0; x < 10; x++) {
                sprites[x][z] = new Sprite(texture);
                sprites[x][z].setPosition(x,z);
                sprites[x][z].setSize(1, 1);
            }
        }

        batch = new SpriteBatch();

        Gdx.input.setInputProcessor(new WorldRendererInput());
    }

    public void render() {
//        renderWorld();
        // TODO

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        controller.getCameraHelper().applyTo(camera);

        batch.setProjectionMatrix(camera.combined);
        batch.setTransformMatrix(controller.getCameraHelper().getMatrix());
        batch.begin();
        for(int z = 0; z < 10; z++) {
            for(int x = 0; x < 10; x++) {
                sprites[x][z].draw(batch);
            }
        }
        batch.end();

        checkTileTouched();
    }

    Sprite lastSelectedTile = null;

    private void checkTileTouched() {
        if (Gdx.input.justTouched()) {
            Vector3 selectedPoint =
                    controller.getCameraHelper().getSelectedPoint(camera, Gdx.input.getX(), Gdx.input.getY());

            int x = (int) selectedPoint.x;
            int z = (int) selectedPoint.z;

            if (x >= 0 && x < 10 && z >= 0 && z < 10) {
                if (lastSelectedTile != null) lastSelectedTile.setColor(1, 1, 1, 1);

                Sprite sprite = sprites[x][z];
                sprite.setColor(1, 0, 0, 1);
                lastSelectedTile = sprite;
            }
        }
    }

    private void renderWorld() {
        controller.getCameraHelper().applyTo(camera);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        controller.getGameState().getLevel().render(batch);
        batch.end();

//        if (DEBUG_DRAW_BOX2D_WORLD) {
//            b2debugRenderer.render(controller.getB2world(), camera.combined);
//        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    private class WorldRendererInput extends InputAdapter {

        final Vector3 last = new Vector3(-1, -1, -1);

        @Override
        public boolean touchDragged(int x, int y, int pointer) {
            Vector3 curr = controller.getCameraHelper().getSelectedPoint(camera, x, y);

            if(!(last.x == -1 && last.y == -1 && last.z == -1)) {
                Vector3 delta = controller.getCameraHelper().getSelectedPoint(camera, last.x, last.y);
                delta.sub(curr);

                controller.getCameraHelper().getPosition().add(delta.x, delta.y, delta.z);
            }

            last.set(x, y, 0);
            return false;
        }

        @Override public boolean touchUp(int x, int y, int pointer, int button) {
            last.set(-1, -1, -1);
            return false;
        }
    }
}
