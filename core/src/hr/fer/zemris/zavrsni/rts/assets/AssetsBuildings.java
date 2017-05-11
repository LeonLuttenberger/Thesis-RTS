package hr.fer.zemris.zavrsni.rts.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static hr.fer.zemris.zavrsni.rts.assets.AssetConstants.GENERATOR_REGION_NAME;
import static hr.fer.zemris.zavrsni.rts.assets.AssetConstants.MANUFACTORY_REGION_NAME;

public class AssetsBuildings {

    public final TextureRegion manufactory;
    public final TextureRegion generator;

    public AssetsBuildings(TextureAtlas atlas) {
        manufactory = atlas.findRegion(MANUFACTORY_REGION_NAME);
        generator = atlas.findRegion(GENERATOR_REGION_NAME);
    }
}