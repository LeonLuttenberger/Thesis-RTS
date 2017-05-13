package hr.fer.zemris.zavrsni.rts.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetOthers {

    public final TextureRegion bullet;

    public AssetOthers(TextureAtlas atlas) {
        bullet = atlas.findRegion(AssetNames.BULLET);
    }
}
