package hr.fer.zemris.zavrsni.rts.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static hr.fer.zemris.zavrsni.rts.assets.AssetConstants.MANUFACTORY_REGION_NAME;

public class AssetsBuildings {

    public final TextureRegion manufactory;

    public AssetsBuildings(TextureAtlas atlas) {
        manufactory = atlas.findRegion(MANUFACTORY_REGION_NAME);
    }
}