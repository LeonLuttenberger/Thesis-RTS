package hr.fer.zemris.zavrsni.rts.objects.units;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.objects.IRangedAttacker;
import hr.fer.zemris.zavrsni.rts.objects.projectiles.Projectile;

import java.util.Optional;

import static hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility.closestHostileTargetInRange;

public abstract class PlayerUnit extends Unit {

    private static final long serialVersionUID = 3711619366946194141L;

    private boolean isSelected = false;

    public PlayerUnit(ILevel level, float width, float height,
                      float defaultSpeed, int maxHealth, float attackRange, int attackPower, float attackCooldown) {
        super(level, width, height, defaultSpeed, maxHealth, attackRange, attackPower, attackCooldown);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }

    @Override
    public void update(float deltaTime) {
        MovementUtility.applyFriendlySeparation(this, level.getPlayerUnits());

        super.update(deltaTime);

        if (!readyForAttack()) return;

        Optional<IDamageable<?>> nearestEnemyOpt = closestHostileTargetInRange(this, level, attackRange);
        if (nearestEnemyOpt.isPresent()) {
            IDamageable<?> target = nearestEnemyOpt.get();
            resetAttackCooldown();

            if (this instanceof IRangedAttacker) {
                Projectile projectile = ((IRangedAttacker<?>) this).rangedAttack(target);
                level.addProjectile(projectile);

            } else {
                target.removeHitPoints(attackPower);
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
