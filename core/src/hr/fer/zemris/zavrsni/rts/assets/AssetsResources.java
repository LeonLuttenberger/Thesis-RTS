package hr.fer.zemris.zavrsni.rts.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static hr.fer.zemris.zavrsni.rts.assets.AssetConstants.BOULDER_REGION_NAME;

public class AssetsResources {

    public final TextureRegion rock;

    public AssetsResources(TextureAtlas atlas) {
        rock = atlas.findRegion(BOULDER_REGION_NAME);
    }
}
