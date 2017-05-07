package hr.fer.zemris.zavrsni.rts.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.util.Constants;
import hr.fer.zemris.zavrsni.rts.util.IGameSettings;

public class MenuScreen extends AbstractGameScreen {

    private static final String TAG = MenuScreen.class.getName();

    private Stage stage;
    private Skin mainMenuSkin;
    private Skin uiSkin;

    // menu
    private Image imgBackground;
    private Button btnNewGame;
    private Button btnSettings;
    private Button btnQuitGame;

    // options
    private Window windowSettings;
    private Button btnSaveSettings;
    private Button btnCancelSettings;
    private CheckBox checkFPS;

    // debug
    private static final float DEBUG_REBUILD_INTERVAL = 5.0f;
    private static final boolean DEBUG_ENABLED = false;
    private float debugRebuildStage;

    public MenuScreen(Game game, IGameSettings gameSettings) {
        super(game, gameSettings);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl20.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (DEBUG_ENABLED) {
            debugRebuildStage -= deltaTime;
            if (debugRebuildStage <= 0) {
                debugRebuildStage = DEBUG_REBUILD_INTERVAL;
                rebuildStage();
            }
        }

        stage.act(deltaTime);
        stage.draw();
        stage.setDebugAll(DEBUG_ENABLED);
    }

    private void rebuildStage() {
        mainMenuSkin = new Skin(
                Gdx.files.internal(Constants.SKIN_UI),
                new TextureAtlas(Constants.TEXTURE_ATLAS_UI)
        );
        uiSkin = Assets.getInstance().getUiSkin();

        // build all layers
        Table layerBackground = buildBackgroundLayer();
        Table layerNavigation = buildMenuNavigation();
        windowSettings = buildOptionsWindowLayer();
        windowSettings.setVisible(false);

        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);

        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(layerBackground);
        stack.add(layerNavigation);
        stack.add(windowSettings);
    }

    private Table buildBackgroundLayer() {
        Table table = new Table();

        imgBackground = new Image(mainMenuSkin, "background");
        table.add(imgBackground);

        return table;
    }

    private Table buildMenuNavigation() {
        Table table = new Table();

        btnNewGame = new TextButton("New Game", uiSkin);
        table.add(btnNewGame);
        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game, gameSettings));
            }
        });
        table.row();

        btnSettings = new TextButton("Settings", uiSkin);
        table.add(btnSettings);
        btnSettings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onOpenSettings();
            }
        });
        table.row();

        btnQuitGame = new TextButton("Quit Game", uiSkin);
        table.add(btnQuitGame);
        btnQuitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        return table;
    }

    private Window buildOptionsWindowLayer() {
        Window window = new Window("Settings", uiSkin);
        window.getTitleLabel().setAlignment(Align.center);

        window.add(buildOptWinDebug()).row();
        window.add(buildOptWinButtons());

        window.pack();
        window.setColor(1, 1, 1, 0.8f);

        return window;
    }

    private Table buildOptWinDebug() {
        Table table = new Table();

        table.pad(10, 10, 0, 10);
        table.columnDefaults(0).padRight(10);
        table.columnDefaults(1).padRight(10);

        checkFPS = new CheckBox("", uiSkin);
        table.add(new Label("FPS Counter", uiSkin));
        table.add(checkFPS);
        table.row();

        return table;
    }

    private Table buildOptWinButtons() {
        Table table = new Table();

        btnSaveSettings = new TextButton("Save", uiSkin);
        table.add(btnSaveSettings);
        btnSaveSettings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onSaveClicked();
            }
        });

        btnCancelSettings = new TextButton("Cancel", uiSkin);
        table.add(btnCancelSettings);
        btnCancelSettings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onCloseSettings();
            }
        });

        return table;
    }

    private void loadSettings() {
        gameSettings.load();
        checkFPS.setChecked(gameSettings.showFPSCounter());
    }

    private void saveSettings() {
        gameSettings.setShowFPSCounter(checkFPS.isChecked());
        gameSettings.save();
    }

    private void onSaveClicked() {
        saveSettings();
        onCloseSettings();
    }

    private void onCloseSettings() {
        btnNewGame.setVisible(true);
        btnSettings.setVisible(true);
        btnQuitGame.setVisible(true);
        windowSettings.setVisible(false);
    }

    private void onOpenSettings() {
        loadSettings();

        btnNewGame.setVisible(false);
        btnSettings.setVisible(false);
        btnQuitGame.setVisible(false);
        windowSettings.setVisible(true);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        rebuildStage();
    }
}
