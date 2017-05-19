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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.common.Constants;
import hr.fer.zemris.zavrsni.rts.common.GameResources;
import hr.fer.zemris.zavrsni.rts.common.IGameSettings;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.common.Level;
import hr.fer.zemris.zavrsni.rts.objects.buildings.TurretBuilding;
import hr.fer.zemris.zavrsni.rts.objects.units.player.SoldierUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.player.WorkerUnit;
import hr.fer.zemris.zavrsni.rts.world.controllers.WorldController;
import hr.fer.zemris.zavrsni.rts.world.controllers.state.BuildingControllerState;
import hr.fer.zemris.zavrsni.rts.world.renderers.HealthBarRenderer;
import hr.fer.zemris.zavrsni.rts.world.renderers.WorldRenderer;

public class GameScreen extends AbstractGameScreen {

    private static float RESOURCE_BAR_HEIGHT = 20;

    private WorldController controller;
    private WorldRenderer renderer;

    private OrthographicCamera camera;

    private HealthBarRenderer healthBarRenderer;

    private Stage stageUI;
    private Skin uiSkin;

    private Label labelFPS;
    private Label labelMinerals;
    private Window interactionMenu;

    public GameScreen(Game game, IGameSettings gameSettings) {
        super(game, gameSettings);

        TiledMap tiledMap = new TmxMapLoader().load(Constants.TILED_MAP_TMX);
        ILevel level = new Level(tiledMap);

        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.translate(level.getWidth() * level.getTileWidth() / 2, level.getHeight() * level.getTileHeight() / 2);
        camera.update();

        SpriteBatch batch = new SpriteBatch();
        controller = new WorldController(level, camera);
        renderer = new WorldRenderer(controller, tiledMap, batch, gameSettings);

        healthBarRenderer = new HealthBarRenderer(controller.getGameState(), gameSettings);
    }

    private void rebuildStage() {
        uiSkin = Assets.getInstance().getUiSkin();

        Table tableDebug = buildDebugDisplay();
        Table resourceBar = buildResourceBar();
        interactionMenu = buildInfoBar();

        Stack stack = new Stack();
        stack.setSize(stageUI.getWidth(), stageUI.getHeight());
        stack.addActor(tableDebug);

        stageUI.clear();
        stageUI.addActor(stack);
        stageUI.addActor(resourceBar);
        stageUI.addActor(interactionMenu);
    }

    private Table buildDebugDisplay() {
        Table table = new Table();

        float screenWidth = stageUI.getWidth();
        float screenHeight = stageUI.getHeight();
        table.setBounds(0, screenHeight - RESOURCE_BAR_HEIGHT, screenWidth, RESOURCE_BAR_HEIGHT);

        if (gameSettings.showFPSCounter()) {
            labelFPS = new Label("", uiSkin);
            table.right().top().add(labelFPS);
        }

        return table;
    }

    private Table buildResourceBar() {
        Table table = new Table();

        float screenWidth = stageUI.getWidth();
        float screenHeight = stageUI.getHeight();

        labelMinerals = new Label("0", uiSkin);
        Image image = new Image(Assets.getInstance().getIcons().minerals);
        image.setScaling(Scaling.fit);

        table.setBounds(0, screenHeight - RESOURCE_BAR_HEIGHT, screenWidth, RESOURCE_BAR_HEIGHT);
        table.align(Align.left);
        table.defaults().padRight(10);

        table.add(image);
        table.add(labelMinerals).spaceRight(20);

        return table;
    }

    private Window buildInfoBar() {
        Window window = new Window("", uiSkin);

        Image image = new Image(Assets.getInstance().getAssetsUI().backgroundTexture);
        window.setBackground(image.getDrawable());

        float screenWidth = stageUI.getWidth();
        float screenHeight = stageUI.getHeight();

        window.setBounds(
                screenWidth / 6, 0,
                2 * screenWidth / 3, screenHeight / 6
        );

        Button btnBuildSoldier = new TextButton("Soldier", uiSkin);
        btnBuildSoldier.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.buildUnit(SoldierUnit::new);
            }
        });

        Button btnWorker = new TextButton("Worker", uiSkin);
        btnWorker.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.buildUnit(WorkerUnit::new);
            }
        });

        Button btnBuildGenerator = new TextButton("Turret", uiSkin);
        btnBuildGenerator.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.setControllerState(new BuildingControllerState(TurretBuilding::new, controller));
            }
        });

        window.add(btnBuildSoldier);
        window.add(btnWorker);
        window.add(btnBuildGenerator);

        return window;
    }

    @Override
    public void render(float deltaTime) {
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
        renderer.setView(camera);
        renderer.render();

        healthBarRenderer.render(camera);

        renderFPSCounter();
        renderResourceBar();

        stageUI.act(deltaTime);
        stageUI.draw();
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

    private void renderResourceBar() {
        labelMinerals.setText(Integer.toString(controller.getGameState().getResources(GameResources.KEY_MINERALS)));
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = Constants.VIEWPORT_HEIGHT;
        camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
        camera.update();

        controller.getControllerState().resize(width, height);
        healthBarRenderer.resize(width, height);
        stageUI.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        controller.resume();

        stageUI = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        InputMultiplexer inputMultiplexer = new InputMultiplexer(stageUI, controller.getInputProcessor());
        Gdx.input.setInputProcessor(inputMultiplexer);

        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Keys.ESCAPE) {
                    game.setScreen(new MenuScreen(game, gameSettings));
                }
                if (keycode == Keys.H) {
                    interactionMenu.setVisible(!interactionMenu.isVisible());
                }

                return false;
            }
        });

        rebuildStage();
    }

    @Override
    public void hide() {
        controller.pause();
        stageUI.dispose();
    }

    public WorldController getController() {
        return controller;
    }
}
