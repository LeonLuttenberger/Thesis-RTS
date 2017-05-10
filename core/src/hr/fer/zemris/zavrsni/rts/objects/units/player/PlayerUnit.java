package hr.fer.zemris.zavrsni.rts.objects.units.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public abstract class PlayerUnit extends Unit {

    public PlayerUnit(Animation<TextureRegion> animation, ILevel level, float width, float height,
                      float defaultSpeed, float maxHealth) {
        super(animation, level, width, height, defaultSpeed, maxHealth);
    }

    @Override
    public boolean isHostile() {
        return false;
    }
}
