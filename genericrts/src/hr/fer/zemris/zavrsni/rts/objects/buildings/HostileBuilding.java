package hr.fer.zemris.zavrsni.rts.objects.buildings;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.common.ILevel;

public abstract class HostileBuilding extends Building {

    public HostileBuilding(TextureRegion region, ILevel level, float width, float height, int maxHitPoints) {
        super(region, level, width, height, maxHitPoints);
    }
}
