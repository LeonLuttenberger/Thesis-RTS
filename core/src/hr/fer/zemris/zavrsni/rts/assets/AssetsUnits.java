package hr.fer.zemris.zavrsni.rts.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import static hr.fer.zemris.zavrsni.rts.assets.AssetConstants.BUG_REGION_NAME;
import static hr.fer.zemris.zavrsni.rts.assets.AssetConstants.SOLDIER_REGION_NAME;
import static hr.fer.zemris.zavrsni.rts.assets.AssetConstants.WORKER_REGION_NAME;

public class AssetsUnits {

    public final Animation<TextureRegion> soldierAnimation;
    public final Animation<TextureRegion> workerAnimation;
    public final Animation<TextureRegion> bugAnimation;

    public AssetsUnits(TextureAtlas atlas) {
        Array<AtlasRegion> soldierRunning = atlas.findRegions(SOLDIER_REGION_NAME);
        soldierAnimation = new Animation<>(1/60f, soldierRunning, PlayMode.LOOP);

        Array<AtlasRegion> workerRunning = atlas.findRegions(WORKER_REGION_NAME);
        workerAnimation = new Animation<>(1 / 60f, workerRunning, PlayMode.LOOP);

        Array<AtlasRegion> bugRunning = atlas.findRegions(BUG_REGION_NAME);
        bugAnimation = new Animation<TextureRegion>(1 / 60f, bugRunning, PlayMode.LOOP);
    }
}
