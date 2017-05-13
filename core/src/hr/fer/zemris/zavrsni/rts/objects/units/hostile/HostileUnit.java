package hr.fer.zemris.zavrsni.rts.objects.units.hostile;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public abstract class HostileUnit extends Unit {

    public HostileUnit(Animation<TextureRegion> animation, ILevel level, float width, float height,
                       float defaultSpeed, int maxHealth, float attackRange, int attackPower, float attackCooldown) {
        super(animation, level, width, height, defaultSpeed, maxHealth, attackRange, attackPower, attackCooldown);
    }

    @Override
    public void update(float deltaTime) {
        MovementUtility.applyFriendlySeparation(this, level.getHostileUnits());
        MovementUtility.applyEnemySeparation(this, level.getPlayerUnits());

        super.update(deltaTime);
    }

    @Override
    public boolean isHostile() {
        return true;
    }
}
