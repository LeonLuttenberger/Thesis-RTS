package hr.fer.zemris.zavrsni.rts.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import static hr.fer.zemris.zavrsni.rts.assets.AssetNames.BUG;
import static hr.fer.zemris.zavrsni.rts.assets.AssetNames.SOLDIER;
import static hr.fer.zemris.zavrsni.rts.assets.AssetNames.WORKER;

public class AssetsUnits {

    public final Animation<TextureRegion> soldierAnimation;
    public final Animation<TextureRegion> workerAnimation;
    public final Animation<TextureRegion> bugAnimation;

    public AssetsUnits(TextureAtlas atlas) {
        Array<AtlasRegion> soldierRunning = atlas.findRegions(SOLDIER);
        soldierAnimation = new Animation<>(1/60f, soldierRunning, PlayMode.LOOP);

        Array<AtlasRegion> workerRunning = atlas.findRegions(WORKER);
        workerAnimation = new Animation<>(1 / 60f, workerRunning, PlayMode.LOOP);

        Array<AtlasRegion> bugRunning = atlas.findRegions(BUG);
        bugAnimation = new Animation<>(1 / 60f, bugRunning, PlayMode.LOOP);
    }
}
