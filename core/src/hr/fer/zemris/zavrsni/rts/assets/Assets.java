package hr.fer.zemris.zavrsni.rts.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import hr.fer.zemris.zavrsni.rts.util.Constants;

import static hr.fer.zemris.zavrsni.rts.util.Constants.TEXTURE_ATLAS_OBJECTS;

public final class Assets implements Disposable {

    private static final String TAG = Assets.class.getName();
    private static final Assets INSTANCE = new Assets();

    private static final String UNIT_REGION_NAME = "pikachu";

    public static Assets getInstance() {
        return INSTANCE;
    }

    private AssetManager assetManager;

    private AssetUnits units;
    private AssetFonts fonts;

    private Assets() {}

    public void init(AssetManager assetManager) {
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
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        // create game resource objects
        units = new AssetUnits(atlas);
        fonts = new AssetFonts();
    }

    @Override
    public void dispose() {
        assetManager.dispose();

        fonts.defaultSmall.dispose();
        fonts.defaultNormal.dispose();
        fonts.defaultLarge.dispose();
    }

    public AssetUnits getUnits() {
        return units;
    }

    public AssetFonts getFonts() {
        return fonts;
    }

    public static class AssetUnits {
        public final TextureAtlas.AtlasRegion simpleUnit;

        public AssetUnits(TextureAtlas atlas) {
            simpleUnit = atlas.findRegion(UNIT_REGION_NAME);
        }
    }

    public static class AssetFonts {
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultLarge;

        public AssetFonts() {
            defaultSmall = new BitmapFont(Gdx.files.internal(Constants.ARIAL_FONT), true);
            defaultNormal = new BitmapFont(Gdx.files.internal(Constants.ARIAL_FONT), true);
            defaultLarge = new BitmapFont(Gdx.files.internal(Constants.ARIAL_FONT), true);

            defaultSmall.getData().setScale(0.75f);
            defaultNormal.getData().setScale(1.0f);
            defaultLarge.getData().setScale(2.0f);

            // enable linear texture filtering for smooth fonts
            defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
            defaultLarge.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
    }
}
