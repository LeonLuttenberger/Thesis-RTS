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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.common.IGameSettings;

public class MenuScreen extends AbstractGameScreen {

    private Stage stage;
    private Skin uiSkin;

    // menu
    private Image imgBackground;
    private Button btnNewGame;
    private Button btnSettings;
    private Button btnQuitGame;

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
        Table layerNavigation = buildMenuNavigation();

        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);

        stack.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stack.add(layerBackground);
        stack.add(layerNavigation);
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

        btnNewGame = new TextButton("New Game", uiSkin);
        table.add(btnNewGame);
        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getScreenManager().pushScreen(new GameScreen(game, gameSettings));
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
        Gdx.input.setInputProcessor(stage);
        rebuildStage();
    }
}
