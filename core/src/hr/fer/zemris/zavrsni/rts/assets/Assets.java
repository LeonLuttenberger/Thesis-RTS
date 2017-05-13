package hr.fer.zemris.zavrsni.rts.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import hr.fer.zemris.zavrsni.rts.util.Constants;

import static hr.fer.zemris.zavrsni.rts.util.Constants.TEXTURE_ATLAS_OBJECTS;
import static hr.fer.zemris.zavrsni.rts.util.Constants.TEXTURE_ATLAS_UI;

public final class Assets implements Disposable {

    private static final String TAG = Assets.class.getName();
    private static Assets instance;

    public static Assets getInstance() {
        if (instance == null) {
            instance = new Assets();
        }

        return instance;
    }

    private AssetManager assetManager;

    private AssetsUnits units;
    private AssetsBuildings buildings;
    private AssetsResources resources;
    private AssetOthers others;

    private AssetsIcons icons;
    private AssetHealthBar healthBar;
    private AssetsUI assetsUI;

    private Skin uiSkin;

    private boolean initialized =  false;

    private Assets() {}

    public void init(AssetManager assetManager) {
        if (initialized) {
            dispose();
            return;
        }
        initialized = true;

        this.assetManager = assetManager;

        // set asset manager error handler
        assetManager.setErrorListener((asset, throwable) -> {
            Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", throwable);
        });

        // load texture atlases
        assetManager.load(TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
        assetManager.load(TEXTURE_ATLAS_UI, TextureAtlas.class);

        // start loading assets and wait until finished
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);

        for (String a : assetManager.getAssetNames()) {
            Gdx.app.debug(TAG, "asset: " + a);
        }

        TextureAtlas objectsAtlas = assetManager.get(TEXTURE_ATLAS_OBJECTS);
        TextureAtlas uiAtlas = assetManager.get(TEXTURE_ATLAS_UI);

        // enable texture filtering for pixel smoothing
        for (Texture t : objectsAtlas.getTextures()) {
            t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }

        // create game resource objects
        units = new AssetsUnits(objectsAtlas);
        buildings = new AssetsBuildings(objectsAtlas);
        resources = new AssetsResources(objectsAtlas);
        others = new AssetOthers(objectsAtlas);

        icons = new AssetsIcons(uiAtlas);
        healthBar = new AssetHealthBar(uiAtlas);
        assetsUI = new AssetsUI(uiAtlas);

        uiSkin = new Skin(
                Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
                new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI)
        );
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    public AssetsUnits getUnits() {
        return units;
    }

    public AssetsBuildings getBuildings() {
        return buildings;
    }

    public AssetsResources getResources() {
        return resources;
    }

    public AssetOthers getOthers() {
        return others;
    }

    public AssetsIcons getIcons() {
        return icons;
    }

    public AssetHealthBar getHealthBar() {
        return healthBar;
    }

    public AssetsUI getAssetsUI() {
        return assetsUI;
    }

    public Skin getUiSkin() {
        return uiSkin;
    }
}
