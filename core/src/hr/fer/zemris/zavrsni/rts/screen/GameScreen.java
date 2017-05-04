package hr.fer.zemris.zavrsni.rts.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.util.Constants;
import hr.fer.zemris.zavrsni.rts.util.IGameSettings;
import hr.fer.zemris.zavrsni.rts.world.ILevel;
import hr.fer.zemris.zavrsni.rts.world.Level;
import hr.fer.zemris.zavrsni.rts.world.controllers.IWorldController;
import hr.fer.zemris.zavrsni.rts.world.controllers.InputController;
import hr.fer.zemris.zavrsni.rts.world.controllers.WorldController;
import hr.fer.zemris.zavrsni.rts.world.renderers.DragBoxRenderer;
import hr.fer.zemris.zavrsni.rts.world.renderers.WorldRenderer;

public class GameScreen extends AbstractGameScreen {

    private static final String TAG = GameScreen.class.getName();

    private IWorldController controller;
    private WorldRenderer renderer;

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private InputController inputController;
    private DragBoxRenderer dragBoxRenderer = new DragBoxRenderer();

    private Stage stageUI;
    private Skin uiSkin;

    private Label labelFPS;

    public GameScreen(Game game, IGameSettings gameSettings) {
        super(game, gameSettings);

        TiledMap tiledMap = new TmxMapLoader().load(Constants.TILED_MAP_TMX);
        ILevel level = new Level(tiledMap);

        batch = new SpriteBatch();
        controller = new WorldController(game, level);
        renderer = new WorldRenderer(controller, tiledMap, batch);

        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.translate(level.getWidth() * level.getTileWidth() / 2, level.getHeight() * level.getTileHeight() / 2);
        camera.update();

        inputController = new InputController(dragBoxRenderer, camera, controller);
    }

    private void rebuildStage() {
        uiSkin = Assets.getInstance().getUiSkin();

        Table layerUI = buildUILayer();

        // assemble stage for menu screen
        stageUI.clear();
        Stack stack = new Stack();
        stageUI.addActor(stack);

        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(layerUI);
    }

    private Table buildUILayer() {
        Table table = new Table();

        if (gameSettings.showFPSCounter()) {
            labelFPS = new Label("", uiSkin);
            table.right().top().add(labelFPS);
        }

        return table;
    }

    @Override
    public void render(float deltaTime) {
        inputController.handleInput(deltaTime);
        controller.update(deltaTime);

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

        stageUI.act(deltaTime);
        stageUI.draw();

        renderFPSCounter();

        dragBoxRenderer.render();
    }

    private void renderFPSCounter() {

        int fps = Gdx.graphics.getFramesPerSecond();

        if (fps >= 59) {
            labelFPS.setColor(Color.GREEN);
        } else if (fps > 30) {
            labelFPS.setColor(Color.YELLOW);
        } else {
            labelFPS.setColor(Color.RED);
        }

        labelFPS.setText(Integer.toString(fps));
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = Constants.VIEWPORT_HEIGHT;
        camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
        camera.update();

        stageUI.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        stageUI = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));
        InputMultiplexer inputMultiplexer = new InputMultiplexer(stageUI, inputController);
        Gdx.input.setInputProcessor(inputMultiplexer);

        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Keys.ESCAPE) {
                    game.setScreen(new MenuScreen(game, gameSettings));
                }
                return false;
            }
        });

        rebuildStage();
    }

    @Override
    public void hide() {
        stageUI.dispose();
    }

    public IWorldController getController() {
        return controller;
    }
}
