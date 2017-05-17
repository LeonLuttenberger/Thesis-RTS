package hr.fer.zemris.zavrsni.rts.objects.units;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.IRangedAttacker;
import hr.fer.zemris.zavrsni.rts.objects.projectiles.Projectile;

import static hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility.closestUnitInRange;

public abstract class PlayerUnit extends Unit {

    private boolean isSelected = false;

    public PlayerUnit(Animation<TextureRegion> animation, ILevel level, float width, float height,
                      float defaultSpeed, int maxHealth, float attackRange, int attackPower, float attackCooldown) {
        super(animation, level, width, height, defaultSpeed, maxHealth, attackRange, attackPower, attackCooldown);
    }

    @Override
    public void update(float deltaTime) {
//        MovementUtility.applyEnemySeparation(this, level.getHostileUnits());
        MovementUtility.applyFriendlySeparation(this, level.getPlayerUnits());

        super.update(deltaTime);

        if (!readyForAttack()) return;

        HostileUnit nearestEnemy = closestUnitInRange(this, level.getHostileUnits(), attackRange);
        if (nearestEnemy != null) {
            resetAttackCooldown();

            if (this instanceof IRangedAttacker) {
                Projectile projectile = ((IRangedAttacker<?>) this).rangedAttack(nearestEnemy);
                level.addProjectile(projectile);

            } else {
                nearestEnemy.removeHitPoints(attackPower);
            }
        }
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
