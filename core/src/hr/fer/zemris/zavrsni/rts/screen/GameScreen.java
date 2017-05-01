package hr.fer.zemris.zavrsni.rts.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.util.Constants;
import hr.fer.zemris.zavrsni.rts.world.Level;
import hr.fer.zemris.zavrsni.rts.world.controller.IWorldController;
import hr.fer.zemris.zavrsni.rts.world.controller.InputController;
import hr.fer.zemris.zavrsni.rts.world.controller.WorldController;
import hr.fer.zemris.zavrsni.rts.world.renderer.DragBoxRenderer;
import hr.fer.zemris.zavrsni.rts.world.renderer.WorldRenderer;

public class GameScreen extends AbstractGameScreen {

    private static final String TAG = GameScreen.class.getName();

    private IWorldController controller;
    private WorldRenderer renderer;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera cameraGUI;

    private InputController inputController;
    private DragBoxRenderer dragBoxRenderer = new DragBoxRenderer();

    public GameScreen(Game game) {
        super(game);

        batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);

        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(true);
        cameraGUI.update();

        TiledMap tiledMap = new TmxMapLoader().load(Constants.TILED_MAP_TMX);
        controller = new WorldController(game, new Level(tiledMap));
        renderer = new WorldRenderer(controller, tiledMap, batch);

        inputController = new InputController(dragBoxRenderer, camera, controller);
        setInputProcessor(inputController);
    }

    @Override
    public void render(float delta) {
        inputController.handleInput(delta);
        controller.update(delta);

        // Sets the clear screen color to: Cornflower Blue
        Gdx.gl20.glClearColor(
                0x64/255.0f,
                0x95/255.0f,
                0xed/255.0f,
                0xff/255.0f
        );

        // Clears the screen
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        renderer.setGhostNewBuilding(inputController.getNewBuilding());
        renderer.setView(camera);
        renderer.render();

        renderGUI(batch);

        dragBoxRenderer.render();
    }

    private void renderGUI(SpriteBatch batch) {
        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();

        renderGUIFPSCounter(batch);

        batch.end();
    }

    private void renderGUIFPSCounter(SpriteBatch batch) {
        float x = cameraGUI.viewportWidth - 40;
        float y = 10;
        int fps = Gdx.graphics.getFramesPerSecond();

        BitmapFont fpsFont = Assets.getInstance().getFonts().defaultSmall;
        if (fps >= 59) {
            fpsFont.setColor(Color.GREEN);
        } else if (fps > 30) {
            fpsFont.setColor(Color.YELLOW);
        } else {
            fpsFont.setColor(Color.RED);
        }

        fpsFont.draw(batch, "FPS: " + fps, x, y);
        fpsFont.setColor(Color.WHITE);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();

        cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
        cameraGUI.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
        cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
    }
}
