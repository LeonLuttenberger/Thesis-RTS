package hr.fer.zemris.zavrsni.rts.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import hr.fer.zemris.zavrsni.rts.util.Constants;

public final class Assets implements Disposable {

    private static final String TAG = Assets.class.getName();
    private static final Assets INSTANCE = new Assets();

    private static final String UNIT_REGION_NAME = "pikachu";

    public static Assets getInstance() {
        return INSTANCE;
    }

    private AssetManager assetManager;

    private AssetUnits units;

    private Assets() {}

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;

        // set asset manager error handler
        assetManager.setErrorListener((asset, throwable) -> {
            Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", throwable);
        });

        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);

        // start loading assets and wait until finished
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);

        for (String a : assetManager.getAssetNames()) {
            Gdx.app.debug(TAG, "asset: " + a);
        }

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

        // enable texture filtering for pixel smoothing
        for (Texture t : atlas.getTextures()) {
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        // create game resource objects
        units = new AssetUnits(atlas);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    public AssetUnits getUnits() {
        return units;
    }

    public static class AssetUnits {
        public final TextureAtlas.AtlasRegion simpleUnit;

        public AssetUnits(TextureAtlas atlas) {
            simpleUnit = atlas.findRegion(UNIT_REGION_NAME);
        }
    }
}
