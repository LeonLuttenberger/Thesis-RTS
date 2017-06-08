package hr.fer.zemris.zavrsni.rts.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.common.IGameSettings;
import hr.fer.zemris.zavrsni.rts.localization.BundleKeys;
import hr.fer.zemris.zavrsni.rts.localization.LocalizationBundle;

public class GameResultScreen extends AbstractGameScreen {

    private static final int BUTTON_WIDTH = 280;

    private Stage stage;
    private Skin uiSkin;

    // menu
    private Table layerNavigation;
    private Image imgBackground;
    private Button btnBack;

    private boolean isWon;

    public GameResultScreen(ScreenManagedGame game, IGameSettings gameSettings, boolean isWon) {
        super(game, gameSettings);
        this.isWon = isWon;
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
        layerNavigation = buildScreen();

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

    private Table buildScreen() {
        Table table = new Table();

        LocalizationBundle bundle = LocalizationBundle.getInstance();

        String text;
        if (isWon) {
            text = bundle.getKey(BundleKeys.VICTORY_MESSAGE);
        } else {
            text = bundle.getKey(BundleKeys.GAME_OVER_MESSAGE);
        }

        Label label = new Label(text, uiSkin);
        table.add(label);
        table.row();

        btnBack = new TextButton(bundle.getKey(BundleKeys.BACK_TO_MENU), uiSkin);
        table.add(btnBack).width(BUTTON_WIDTH);
        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getScreenManager().popScreen(2);
            }
        });
        table.row();

        return table;
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
