package hr.fer.zemris.zavrsni.rts.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
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
import hr.fer.zemris.zavrsni.rts.localization.BundleKeys;
import hr.fer.zemris.zavrsni.rts.localization.LocalizationBundle;

public class OptionsScreen extends AbstractGameScreen {

    private Stage stage;
    private Skin uiSkin;

    private Image imgBackground;

    private Button btnSaveSettings;
    private Button btnCancelSettings;
    private CheckBox checkFPS;
    private CheckBox checkPathfindingRoutes;
    private CheckBox checkPlayerHealthBars;
    private CheckBox checkHostileHealthBars;
    private CheckBox checkResourceHealthBars;
    private CheckBox checkBuildingHealthBars;

    private SelectBox<String> selectLanguages;

    public OptionsScreen(ScreenManagedGame game, IGameSettings gameSettings) {
        super(game, gameSettings);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl20.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(deltaTime);
        stage.draw();
    }

    private Table buildBackgroundLayer() {
        Table table = new Table();

        imgBackground = new Image(Assets.getInstance().getAssetsUI().backgroundImage);
        imgBackground.setScaling(Scaling.fill);
        imgBackground.setSize(stage.getViewport().getScreenWidth(), stage.getViewport().getScreenHeight());
        table.add(imgBackground);

        return table;
    }

    private void rebuildStage() {
        uiSkin = Assets.getInstance().getUiSkin();

        // build all layers
        Table layerBackground = buildBackgroundLayer();
        Window windowSettings = buildOptionsWindowLayer();

        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);

        stack.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stack.add(layerBackground);
        stack.add(windowSettings);

        loadSettings();
    }

    private Window buildOptionsWindowLayer() {
        Window window = new Window(LocalizationBundle.getInstance().getKey(BundleKeys.SETTINGS), uiSkin);
        window.getTitleLabel().setAlignment(Align.center);

        window.add(buildOptionWindow()).row();
        window.add(buildOptWinButtons());

        window.pack();
        window.setColor(1, 1, 1, 0.8f);

        return window;
    }

    private Table buildOptionWindow() {
        Table table = new Table();
        LocalizationBundle bundle = LocalizationBundle.getInstance();

        table.pad(10, 10, 0, 10);
        table.columnDefaults(0).padRight(10);
        table.columnDefaults(1).padRight(10);

        checkFPS = new CheckBox("", uiSkin);
        table.add(new Label(bundle.getKey(BundleKeys.FPS_COUNTER), uiSkin));
        table.add(checkFPS);
        table.row();

        checkPathfindingRoutes = new CheckBox("", uiSkin);
        table.add(new Label(bundle.getKey(BundleKeys.PATHFINDING_ROUTES), uiSkin));
        table.add(checkPathfindingRoutes);
        table.row();

        checkPlayerHealthBars = new CheckBox("", uiSkin);
        table.add(new Label(bundle.getKey(BundleKeys.PLAYER_HEALTH_BAR), uiSkin));
        table.add(checkPlayerHealthBars);
        table.row();

        checkHostileHealthBars = new CheckBox("", uiSkin);
        table.add(new Label(bundle.getKey(BundleKeys.HOSTILE_HEALTH_BAR), uiSkin));
        table.add(checkHostileHealthBars);
        table.row();

        checkResourceHealthBars = new CheckBox("", uiSkin);
        table.add(new Label(bundle.getKey(BundleKeys.RESOURCE_HEALTH_BAR), uiSkin));
        table.add(checkResourceHealthBars);
        table.row();

        checkBuildingHealthBars = new CheckBox("", uiSkin);
        table.add(new Label(bundle.getKey(BundleKeys.BUILDING_HEALTH_BAR), uiSkin));
        table.add(checkBuildingHealthBars);
        table.row();

        selectLanguages = new SelectBox<>(uiSkin);
        selectLanguages.setItems("en", "hr");
        table.add(new Label(bundle.getKey(BundleKeys.LANGUAGE), uiSkin));
        table.add(selectLanguages);
        table.row();

        return table;
    }

    private Table buildOptWinButtons() {
        Table table = new Table();
        LocalizationBundle bundle = LocalizationBundle.getInstance();

        btnSaveSettings = new TextButton(bundle.getKey(BundleKeys.SAVE), uiSkin);
        table.add(btnSaveSettings);
        btnSaveSettings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onSaveClicked();
            }
        });

        btnCancelSettings = new TextButton(bundle.getKey(BundleKeys.CANCEL), uiSkin);
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
        checkPathfindingRoutes.setChecked(gameSettings.showPathFindingRoutes());
        checkPlayerHealthBars.setChecked(gameSettings.showPlayerUnitHealthBars());
        checkHostileHealthBars.setChecked(gameSettings.showHostileUnitHealthBars());
        checkResourceHealthBars.setChecked(gameSettings.showResourceHealthBars());
        checkBuildingHealthBars.setChecked(gameSettings.showBuildingHealthBars());
        selectLanguages.setSelected(gameSettings.getLocaleTag());
    }

    private void saveSettings() {
        gameSettings.setShowFPSCounter(checkFPS.isChecked());
        gameSettings.setShowPathFindingRoutes(checkPathfindingRoutes.isChecked());
        gameSettings.setShowPlayerUnitHealthBars(checkPlayerHealthBars.isChecked());
        gameSettings.setShowHostileUnitHealthBars(checkHostileHealthBars.isChecked());
        gameSettings.setShowResourceHealthBars(checkResourceHealthBars.isChecked());
        gameSettings.setShowBuildingHealthBars(checkBuildingHealthBars.isChecked());
        gameSettings.setLocaleTag(selectLanguages.getSelected());

        gameSettings.save();

        LocalizationBundle.getInstance().loadLocale(gameSettings);
    }

    private void onSaveClicked() {
        saveSettings();
        onCloseSettings();
    }

    private void onCloseSettings() {
        game.getScreenManager().popScreen();
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
