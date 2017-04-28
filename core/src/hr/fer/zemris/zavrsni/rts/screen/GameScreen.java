package hr.fer.zemris.zavrsni.rts.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import hr.fer.zemris.zavrsni.rts.util.Constants;
import hr.fer.zemris.zavrsni.rts.world.IWorldController;
import hr.fer.zemris.zavrsni.rts.world.InputController;
import hr.fer.zemris.zavrsni.rts.world.Level;
import hr.fer.zemris.zavrsni.rts.world.WorldController;
import hr.fer.zemris.zavrsni.rts.world.WorldRenderer;

public class GameScreen extends AbstractGameScreen {

    private static final String TAG = GameScreen.class.getName();

    private IWorldController controller;
    private WorldRenderer renderer;

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private InputController inputController;
    private DragBoxRenderer dragBoxRenderer = new DragBoxRenderer();

    public GameScreen(Game game) {
        super(game);

        batch = new SpriteBatch();
        camera = new OrthographicCamera();

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
        renderer.setView(camera);
        renderer.render();

        dragBoxRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }
}
