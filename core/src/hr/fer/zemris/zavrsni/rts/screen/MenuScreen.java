package hr.fer.zemris.zavrsni.rts.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
import hr.fer.zemris.zavrsni.rts.common.IGameSettings;
import hr.fer.zemris.zavrsni.rts.common.IGameState;
import hr.fer.zemris.zavrsni.rts.localization.BundleKeys;
import hr.fer.zemris.zavrsni.rts.localization.LocalizationBundle;

public class MenuScreen extends AbstractGameScreen {

    private static final int BUTTON_WIDTH = 280;

    private Stage stage;
    private Skin uiSkin;

    // menu
    private Table layerNavigation;
    private Image imgBackground;
    private Button btnNewGame;
    private Button btnLoadGame;
    private Button btnSettings;
    private Button btnQuitGame;

    private Window layerLoadSave;

    public MenuScreen(ScreenManagedGame game, IGameSettings gameSettings) {
        super(game, gameSettings);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl20.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(deltaTime);
        stage.draw();
    }

    private void rebuildStage() {
        uiSkin = Assets.getInstance().getUiSkin();

        // build all layers
        Table layerBackground = buildBackgroundLayer();
        layerNavigation = buildMenuNavigation();

        layerLoadSave = new Window(LocalizationBundle.getInstance().getKey(BundleKeys.LOAD_GAME), uiSkin);
        layerLoadSave.getTitleLabel().setAlignment(Align.center);
        layerLoadSave.setVisible(false);

        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);

        stack.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stack.add(layerBackground);
        stack.add(layerNavigation);
        stack.add(layerLoadSave);
    }

    private Table buildBackgroundLayer() {
        Table table = new Table();

        imgBackground = new Image(Assets.getInstance().getAssetsUI().backgroundImage);
        imgBackground.setScaling(Scaling.fill);
        imgBackground.setSize(stage.getViewport().getScreenWidth(), stage.getViewport().getScreenHeight());
        table.add(imgBackground);

        return table;
    }

    private Table buildMenuNavigation() {
        Table table = new Table();

        LocalizationBundle bundle = LocalizationBundle.getInstance();

        btnNewGame = new TextButton(bundle.getKey(BundleKeys.NEW_GAME), uiSkin);
        table.add(btnNewGame).width(BUTTON_WIDTH);
        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getScreenManager().pushGameScreen();
            }
        });
        table.row();

        btnLoadGame = new TextButton(bundle.getKey(BundleKeys.LOAD_GAME), uiSkin);
        table.add(btnLoadGame).width(BUTTON_WIDTH);
        btnLoadGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                layerLoadSave.clear();

                for (String save : SaveGameUtils.showSaves()) {
                    final String s = save;

                    Button btnLoadSave = new TextButton(save, uiSkin);
                    layerLoadSave.add(btnLoadSave).width(BUTTON_WIDTH);
                    btnLoadSave.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            IGameState gameState = SaveGameUtils.loadSave(s);
                            if (gameState != null) {
                                game.screenManager.pushGameScreen(gameState);
                            }
                        }
                    });
                    layerLoadSave.row();
                }

                Button btnBack = new TextButton(bundle.getKey(BundleKeys.CANCEL), uiSkin);
                layerLoadSave.add(btnBack).width(BUTTON_WIDTH);
                btnBack.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        layerLoadSave.setVisible(false);
                        layerNavigation.setVisible(true);
                    }
                });
                layerLoadSave.row();

                layerLoadSave.setVisible(true);
                layerNavigation.setVisible(false);
            }
        });
        table.row();

        btnSettings = new TextButton(bundle.getKey(BundleKeys.SETTINGS), uiSkin);
        table.add(btnSettings).width(BUTTON_WIDTH);
        btnSettings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onOpenSettings();
            }
        });
        table.row();

        btnQuitGame = new TextButton(bundle.getKey(BundleKeys.EXIT_GAME), uiSkin);
        table.add(btnQuitGame).width(BUTTON_WIDTH);
        btnQuitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        return table;
    }

    private void onOpenSettings() {
        game.getScreenManager().pushOptionsScreen();
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
        stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);
        rebuildStage();
    }
}
