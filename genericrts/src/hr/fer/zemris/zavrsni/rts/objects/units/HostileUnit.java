package hr.fer.zemris.zavrsni.rts.objects.units;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.objects.IRangedAttacker;
import hr.fer.zemris.zavrsni.rts.objects.projectiles.Projectile;

import java.util.Optional;

import static hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility.applyEnemySeparation;
import static hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility.applyFriendlySeparation;
import static hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility.closestPlayerTargetInRange;
import static hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility.closestUnitInRange;

public abstract class HostileUnit extends Unit {

    protected final float enemyDetectionRange;

    public HostileUnit(Animation<TextureRegion> animation, ILevel level, float width, float height, float defaultSpeed,
            int maxHealth, float attackRange, int attackPower, float attackCooldown, float enemyDetectionRange) {

        super(animation, level, width, height, defaultSpeed, maxHealth, attackRange, attackPower, attackCooldown);
        this.enemyDetectionRange = enemyDetectionRange;
    }

    @Override
    public void update(float deltaTime) {
        if (isSearchStopped()) {
            PlayerUnit nearestThreat = closestUnitInRange(this, level.getPlayerUnits(), enemyDetectionRange);

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
            }
        }

        applyEnemySeparation(this, level.getPlayerUnits());
        applyFriendlySeparation(this, level.getHostileUnits());

        super.update(deltaTime);

        if (!readyForAttack()) return;

        Optional<IDamageable<?>> nearestEnemyOpt = closestPlayerTargetInRange(this, level, attackRange);
        if (nearestEnemyOpt.isPresent()) {
            resetAttackCooldown();
            IDamageable<?> nearestEnemy = nearestEnemyOpt.get();

            if (this instanceof IRangedAttacker) {
                Projectile projectile = ((IRangedAttacker<?>) this).rangedAttack(nearestEnemy);
                level.addProjectile(projectile);

            } else {
                nearestEnemy.removeHitPoints(attackPower);
            }
        }
    }
}
