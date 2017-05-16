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

        PlayerUnit nearestThreat = MovementUtility.closestUnitInRange(this, playerUnits, ENEMY_DETECTION_RANGE);
        if (nearestThreat != null) {
            float dx = nearestThreat.getCenterX() - getCenterX();
            float dy = nearestThreat.getCenterY() - getCenterY();

            float length = (float) Math.sqrt(dx * dx + dy * dy);
            dx /= length;
            dy /= length;

            float speed = level.getTerrainModifier(getCenterX(), getCenterY()) * defaultSpeed;
            adjustDirection(
                    speed * dx,
                    speed * dy
            );

            float distance = distanceBetween(this, nearestThreat);
            if (distance < attackRange && readyForAttack()) {
                resetAttackCooldown();
                nearestThreat.removeHitPoints(attackPower);
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
