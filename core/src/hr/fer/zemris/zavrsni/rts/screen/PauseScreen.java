package hr.fer.zemris.zavrsni.rts.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
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

import java.util.Optional;

public class PauseScreen extends AbstractGameScreen {

    private static final int BUTTON_WIDTH = 280;

    private Stage stage;
    private Skin uiSkin;

    // menu
    private Table layerNavigation;
    private Image imgBackground;
    private Button btnContinue;
    private Button btnSave;
    private Button btnSettings;
    private Button btnMainMenu;

    private Window layerSaveGame;

    public PauseScreen(ScreenManagedGame game, IGameSettings gameSettings) {
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

        layerSaveGame = new Window(LocalizationBundle.getInstance().getKey(BundleKeys.SAVE_GAME), uiSkin);
        layerSaveGame.getTitleLabel().setAlignment(Align.center);
        layerSaveGame.setVisible(false);

        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);

        stack.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stack.add(layerBackground);
        stack.add(layerNavigation);
        stack.add(layerSaveGame);
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

        btnContinue = new TextButton(bundle.getKey(BundleKeys.CONTINUE), uiSkin);
        table.add(btnContinue).width(BUTTON_WIDTH);
        btnContinue.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getScreenManager().popScreen();
            }
        });
        table.row();

        btnSave = new TextButton(bundle.getKey(BundleKeys.SAVE_GAME), uiSkin);
        table.add(btnSave).width(BUTTON_WIDTH);
        btnSave.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Optional<GameScreen> gameScreenOpt = game.getScreenManager().getGameScreen();
                if (!gameScreenOpt.isPresent()) return;

                GameScreen gameScreen = gameScreenOpt.get();
                IGameState gameState = gameScreen.getController().getGameState();

                layerSaveGame.clear();

                TextField tfSave = new TextField("", uiSkin);
                layerSaveGame.add(tfSave).width(BUTTON_WIDTH);
                layerSaveGame.row();

                Button btnConfirm = new TextButton(bundle.getKey(BundleKeys.SAVE), uiSkin);
                layerSaveGame.add(btnConfirm).width(BUTTON_WIDTH);
                btnConfirm.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (tfSave.getText().isEmpty()) return;

                        SaveGameUtils.saveGame(tfSave.getText(), gameState);
                        layerSaveGame.setVisible(false);
                        layerNavigation.setVisible(true);
                    }
                });
                layerSaveGame.row();

                Button btnBack = new TextButton(bundle.getKey(BundleKeys.CANCEL), uiSkin);
                layerSaveGame.add(btnBack).width(BUTTON_WIDTH);
                btnBack.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        layerSaveGame.setVisible(false);
                        layerNavigation.setVisible(true);
                    }
                });
                layerSaveGame.row();

                layerSaveGame.setVisible(true);
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

        btnMainMenu = new TextButton(bundle.getKey(BundleKeys.BACK_TO_MENU), uiSkin);
        table.add(btnMainMenu).width(BUTTON_WIDTH);
        btnMainMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getScreenManager().popScreen(2);
            }
        });

        return table;
    }

    private void onOpenSettings() {
        game.getScreenManager().pushScreen(new OptionsScreen(game, gameSettings));
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

        InputMultiplexer inputMultiplexer = new InputMultiplexer(stage);
        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Keys.ESCAPE) {
                    game.getScreenManager().popScreen();
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);

        rebuildStage();
    }
}
