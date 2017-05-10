package hr.fer.zemris.zavrsni.rts.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static hr.fer.zemris.zavrsni.rts.assets.AssetConstants.HEALTH_BAR_HIT_REGION_NAME;
import static hr.fer.zemris.zavrsni.rts.assets.AssetConstants.HEALTH_BAR_REGION_NAME;

public class AssetHealthBar {

    public final List<AtlasRegion> healthBars;
    public final AtlasRegion healthBarHit;

    public AssetHealthBar(TextureAtlas atlas) {
        Array<AtlasRegion> regions = atlas.findRegions(HEALTH_BAR_REGION_NAME);
        healthBarHit = atlas.findRegion(HEALTH_BAR_HIT_REGION_NAME);

        List<AtlasRegion> list = new ArrayList<>();
        for (AtlasRegion region : regions) {
            if (region != null) {
                list.add(region);
            }
        }
        healthBars = Collections.unmodifiableList(list);
    }
}
