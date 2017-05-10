package hr.fer.zemris.zavrsni.rts.objects.units.hostile;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public abstract class HostileUnit extends Unit {

    public HostileUnit(Animation<TextureRegion> animation, ILevel level, float width, float height,
                       float defaultSpeed, int maxHealth, float attackRange, int attackPower) {
        super(animation, level, width, height, defaultSpeed, maxHealth, attackRange, attackPower);
    }

    @Override
    public boolean isHostile() {
        return true;
    }
}
