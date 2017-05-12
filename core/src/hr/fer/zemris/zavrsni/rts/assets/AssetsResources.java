package hr.fer.zemris.zavrsni.rts.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static hr.fer.zemris.zavrsni.rts.assets.AssetNames.BOULDER;

public class AssetsResources {

    public final TextureRegion rock;

    public AssetsResources(TextureAtlas atlas) {
        rock = atlas.findRegion(BOULDER);
    }
}
