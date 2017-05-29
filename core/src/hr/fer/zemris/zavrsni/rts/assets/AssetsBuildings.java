package hr.fer.zemris.zavrsni.rts.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static hr.fer.zemris.zavrsni.rts.assets.AssetNames.ALIEN_BASE;
import static hr.fer.zemris.zavrsni.rts.assets.AssetNames.GENERATOR;
import static hr.fer.zemris.zavrsni.rts.assets.AssetNames.MANUFACTORY;

public class AssetsBuildings {

    public final TextureRegion manufactory;
    public final TextureRegion generator;
    public final TextureRegion alienBase;

    public final TextureRegion stickmanSpawner;

    public AssetsBuildings(TextureAtlas atlas) {
        manufactory = atlas.findRegion(MANUFACTORY);
        generator = atlas.findRegion(GENERATOR);
        alienBase = atlas.findRegion(ALIEN_BASE);

        stickmanSpawner = atlas.findRegion("stickman_spawner");
    }
}