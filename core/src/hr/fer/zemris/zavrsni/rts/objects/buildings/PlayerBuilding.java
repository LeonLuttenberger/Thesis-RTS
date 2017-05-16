package hr.fer.zemris.zavrsni.rts.objects.buildings;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public abstract class PlayerBuilding extends Building {

    public PlayerBuilding(TextureRegion region, ILevel level, float width, float height, int maxHitPoints) {
        super(region, level, width, height, maxHitPoints);
    }
}
