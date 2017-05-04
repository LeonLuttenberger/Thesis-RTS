package hr.fer.zemris.zavrsni.rts.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import hr.fer.zemris.zavrsni.rts.util.Constants;

import static hr.fer.zemris.zavrsni.rts.util.Constants.TEXTURE_ATLAS_OBJECTS;

public final class Assets implements Disposable {

    private static final String TAG = Assets.class.getName();
    private static Assets instance;

    private static final String SOLDIER_REGION_NAME = "soldier";
    private static final String MANUFACTORY_REGION_NAME = "manufactory";
    private static final String BOULDER_REGION_NAME = "boulder";

    public static Assets getInstance() {
        if (instance == null) {
            instance = new Assets();
        }

        return instance;
    }

    private AssetManager assetManager;

    private AssetUnits units;
    private AssetBuildings buildings;
    private AssetResources resources;

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
        units = new AssetUnits(atlas);
        buildings = new AssetBuildings(atlas);
        resources = new AssetResources(atlas);

        uiSkin = new Skin(
                Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
                new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI)
        );
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    public AssetUnits getUnits() {
        return units;
    }

    public AssetBuildings getBuildings() {
        return buildings;
    }

    public AssetResources getResources() {
        return resources;
    }

    public Skin getUiSkin() {
        return uiSkin;
    }

    public static class AssetUnits {
        public final Animation<TextureRegion> soldierAnimation;

        public AssetUnits(TextureAtlas atlas) {
            Array<AtlasRegion> soldierRunning = atlas.findRegions(SOLDIER_REGION_NAME);
            soldierAnimation = new Animation<>(1/60f, soldierRunning, PlayMode.LOOP);
        }
    }

    public static class AssetBuildings {
        public final TextureRegion manufactory;

        public AssetBuildings(TextureAtlas atlas) {
            manufactory = atlas.findRegion(MANUFACTORY_REGION_NAME);
        }
    }

    public static class AssetResources {
        public final TextureRegion rock;

        public AssetResources(TextureAtlas atlas) {
            rock = atlas.findRegion(BOULDER_REGION_NAME);
        }
    }
}
