package hr.fer.zemris.zavrsni.rts.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetsUI {

    public final TextureRegion backgroundImage;
    public final TextureRegion backgroundTexture;

    public AssetsUI(TextureAtlas atlas) {
        backgroundImage = atlas.findRegion(AssetNames.WALLPAPER);
        backgroundTexture = atlas.findRegion(AssetNames.MENU_TEXTURE);
    }
}
