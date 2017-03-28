package hr.fer.zemris.zavrsni.rts.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import hr.fer.zemris.zavrsni.rts.util.Constants;

public final class Assets implements Disposable {

    private static final String TAG = Assets.class.getName();
    private static final Assets INSTANCE = new Assets();

    public static Assets getInstance() {
        return INSTANCE;
    }

    private AssetManager assetManager;

    private Assets() {}

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;

        // set asset manager error handler
        assetManager.setErrorListener((asset, throwable) -> {
            Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", throwable);
        });

        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
