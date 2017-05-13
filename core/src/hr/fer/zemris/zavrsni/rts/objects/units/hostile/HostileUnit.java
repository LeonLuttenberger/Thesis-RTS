package hr.fer.zemris.zavrsni.rts.objects.units.hostile;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;
import hr.fer.zemris.zavrsni.rts.objects.units.player.PlayerUnit;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

import java.util.List;

public abstract class HostileUnit extends Unit {

    private static final float ENEMY_DETECTION_RANGE = 150f;

    public HostileUnit(Animation<TextureRegion> animation, ILevel level, float width, float height,
                       float defaultSpeed, int maxHealth, float attackRange, int attackPower, float attackCooldown) {
        super(animation, level, width, height, defaultSpeed, maxHealth, attackRange, attackPower, attackCooldown);
    }

    @Override
    public void update(float deltaTime) {
        List<HostileUnit> hostileUnits = level.getHostileUnits();
        List<PlayerUnit> playerUnits = level.getPlayerUnits();

        PlayerUnit nearestPlayerUnit = MovementUtility.closestUnit(this, playerUnits);
        if (nearestPlayerUnit != null) {
            float distance = distanceBetween(this, nearestPlayerUnit);

            if (distance < ENEMY_DETECTION_RANGE) {
                float dx = nearestPlayerUnit.getCenterX() - getCenterX();
                float dy = nearestPlayerUnit.getCenterY() - getCenterY();

                float length = (float) Math.sqrt(dx * dx + dy * dy);
                dx /= length;
                dy /= length;

                float speed = level.getTerrainModifier(getCenterX(), getCenterY()) * defaultSpeed;
                adjustDirection(
                        speed * dx,
                        speed * dy
                );
            }

            if (distance < attackRange && readyForAttack()) {
                resetAttackCooldown();
                nearestPlayerUnit.removeHitPoints(attackPower);
            }
        }

        MovementUtility.applyFriendlySeparation(this, hostileUnits);
        MovementUtility.applyEnemySeparation(this, playerUnits);

        super.update(deltaTime);
    }

    @Override
    public boolean isHostile() {
        return true;
    }
}
