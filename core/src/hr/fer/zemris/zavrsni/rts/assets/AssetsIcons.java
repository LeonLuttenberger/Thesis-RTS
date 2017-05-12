package hr.fer.zemris.zavrsni.rts.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetsIcons {

    public final TextureRegion minerals;

    public AssetsIcons(TextureAtlas atlas) {
        minerals = atlas.findRegion(AssetNames.ICON_MINERALS);
    }
}
