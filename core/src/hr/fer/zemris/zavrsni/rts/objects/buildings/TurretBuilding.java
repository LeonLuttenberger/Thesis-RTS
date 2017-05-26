package hr.fer.zemris.zavrsni.rts.objects.buildings;

import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.objects.IRangedAttacker;
import hr.fer.zemris.zavrsni.rts.objects.projectiles.PlayerBullet;
import hr.fer.zemris.zavrsni.rts.objects.projectiles.Projectile;
import hr.fer.zemris.zavrsni.rts.objects.units.HostileUnit;

import static hr.fer.zemris.zavrsni.rts.objects.units.MovementUtility.closestUnitInRange;

public class TurretBuilding extends PlayerBuilding implements IRangedAttacker<TurretBuilding> {

    private static final int WIDTH = 80;
    private static final int HEIGHT = 128;
    private static final int MAX_HIT_POINTS = 100;

    private static final float ATTACK_COOLDOWN = 2;
    private static final int ATTACK_POWER = 20;
    private static final float ATTACK_RANGE = 300f;

    private float timeSinceLastAttack = 0;

    public TurretBuilding(ILevel level) {
        super(Assets.getInstance().getBuildings().generator, level, WIDTH, HEIGHT, MAX_HIT_POINTS);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        timeSinceLastAttack += deltaTime;

        if (!readyForAttack()) return;

        HostileUnit nearestEnemy = closestUnitInRange(this, level.getHostileUnits(), ATTACK_RANGE);
        if (nearestEnemy != null) {
            resetAttackCooldown();

            Projectile projectile = this.rangedAttack(nearestEnemy);
            level.addProjectile(projectile);
        }
    }

    @Override
    public Projectile rangedAttack(IDamageable<? extends AbstractGameObject> target) {
        return new PlayerBullet(level, this, target, ATTACK_POWER);
    }

    @Override
    public float getAttackRange() {
        return ATTACK_RANGE;
    }

    private boolean readyForAttack() {
        return timeSinceLastAttack > ATTACK_COOLDOWN;
    }

    private void resetAttackCooldown() {
        timeSinceLastAttack = 0;
    }
}
