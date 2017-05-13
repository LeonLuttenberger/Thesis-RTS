package hr.fer.zemris.zavrsni.rts.objects.units.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.objects.projectiles.Projectile;
import hr.fer.zemris.zavrsni.rts.objects.units.IRangedUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;
import hr.fer.zemris.zavrsni.rts.objects.units.hostile.HostileUnit;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

import java.util.List;

public abstract class PlayerUnit extends Unit {

    private boolean isSelected = false;

    public PlayerUnit(Animation<TextureRegion> animation, ILevel level, float width, float height,
                      float defaultSpeed, int maxHealth, float attackRange, int attackPower, float attackCooldown) {
        super(animation, level, width, height, defaultSpeed, maxHealth, attackRange, attackPower, attackCooldown);
    }

    @Override
    public void update(float deltaTime) {
        MovementUtility.applyEnemySeparation(this, level.getHostileUnits());
        MovementUtility.applyFriendlySeparation(this, level.getPlayerUnits());

        super.update(deltaTime);

        if (!readyForAttack()) return;

        Unit nearestEnemy = closestEnemyUnit(level.getHostileUnits());
        if (nearestEnemy != null && distanceBetween(this, nearestEnemy) < attackRange) {
            resetAttackCooldown();

            if (this instanceof IRangedUnit) {
                Projectile projectile = ((IRangedUnit) this).rangedAttack(nearestEnemy);
                level.addProjectile(projectile);

            } else {
                nearestEnemy.removeHitPoints(attackPower);
            }
        }
    }

    private HostileUnit closestEnemyUnit(List<HostileUnit> enemyUnits) {
        HostileUnit closestEnemy = null;
        float minDistance = Float.POSITIVE_INFINITY;

        for (HostileUnit enemy : enemyUnits) {
            float distance = distanceBetween(this, enemy);
            if (closestEnemy == null || distance < minDistance) {
                minDistance = distance;
                closestEnemy = enemy;
            }
        }

        return closestEnemy;
    }

    @Override
    public boolean isHostile() {
        return false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
