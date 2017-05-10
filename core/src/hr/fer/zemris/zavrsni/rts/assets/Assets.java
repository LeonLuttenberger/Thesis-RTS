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
    private AssetsIcons icons;
    private AssetHealthBar healthBar;

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

        // load texture atlas
        assetManager.load(TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);

        // start loading assets and wait until finished
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);

        for (String a : assetManager.getAssetNames()) {
            Gdx.app.debug(TAG, "asset: " + a);
        }

        TextureAtlas atlas = assetManager.get(TEXTURE_ATLAS_OBJECTS);

        // enable texture filtering for pixel smoothing
        for (Texture t : atlas.getTextures()) {
            t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }

        // create game resource objects
        units = new AssetsUnits(atlas);
        buildings = new AssetsBuildings(atlas);
        resources = new AssetsResources(atlas);
        icons = new AssetsIcons(atlas);
        healthBar = new AssetHealthBar(atlas);

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

    public AssetsIcons getIcons() {
        return icons;
    }

    public AssetHealthBar getHealthBar() {
        return healthBar;
    }

    public Skin getUiSkin() {
        return uiSkin;
    }
}
