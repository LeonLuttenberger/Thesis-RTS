package hr.fer.zemris.zavrsni.rts.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetsUI {

    private final TextureRegion backgroundImage;
    private final TextureRegion backgroundTexture;

    public AssetsUI(TextureAtlas atlas) {
        backgroundImage = atlas.findRegion(AssetNames.WALLPAPER);
        backgroundTexture = atlas.findRegion(AssetNames.MENU_TEXTURE);
    }
}
