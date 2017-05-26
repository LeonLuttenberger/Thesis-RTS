package hr.fer.zemris.zavrsni.rts.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetOthers {

    public final TextureRegion bullet;
    public final TextureRegion alienBullet;

    public AssetOthers(TextureAtlas atlas) {
        bullet = atlas.findRegion(AssetNames.BULLET);
        alienBullet = atlas.findRegion(AssetNames.ALIEN_BULLET);
    }
}
