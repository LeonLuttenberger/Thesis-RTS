package hr.fer.zemris.zavrsni.rts.objects.units;

import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.objects.IRangedAttacker;
import hr.fer.zemris.zavrsni.rts.objects.projectiles.Projectile;

import java.util.Optional;

import static hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility.applyEnemySeparation;
import static hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility.applyFriendlySeparation;
import static hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility.applyPlayerBuildingSeparation;
import static hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility.applyTerrainSeparation;
import static hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility.closestPlayerTargetInRange;

public abstract class HostileUnit extends Unit {

    private static final long serialVersionUID = 7702814648111303327L;

    protected final float enemyDetectionRange;

    public HostileUnit(ILevel level, float width, float height, float defaultSpeed,
            int maxHealth, float attackRange, int attackPower, float attackCooldown, float enemyDetectionRange) {

        super(level, width, height, defaultSpeed, maxHealth, attackRange, attackPower, attackCooldown);
        this.enemyDetectionRange = enemyDetectionRange;
    }

    @Override
    public void update(float deltaTime) {
        if (isSearchStopped()) {
            Optional<IDamageable<?>> nearestThreatOpt = closestPlayerTargetInRange(this, level, enemyDetectionRange);

            if (nearestThreatOpt.isPresent()) {
                AbstractGameObject nearestThreat = nearestThreatOpt.get().getObject();

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

        applyTerrainSeparation(this, level);
        applyEnemySeparation(this, level.getPlayerUnits());
        applyPlayerBuildingSeparation(this, level);
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

    @Override
    public float getAttackRange() {
        return super.getAttackRange();
    }
}
