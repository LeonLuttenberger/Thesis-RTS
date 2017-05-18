package hr.fer.zemris.zavrsni.rts.objects.units.hostile;

import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.objects.IRangedAttacker;
import hr.fer.zemris.zavrsni.rts.objects.projectiles.Bullet;
import hr.fer.zemris.zavrsni.rts.objects.projectiles.Projectile;
import hr.fer.zemris.zavrsni.rts.objects.units.HostileUnit;

public class AlienSoldierUnit extends HostileUnit implements IRangedAttacker<AlienSoldierUnit> {

    private static final float ENEMY_DETECTION_RANGE = 150f;

    private static final int UNIT_WIDTH = 32;
    private static final int UNIT_HEIGHT = 32;
    private static final float DEFAULT_SPEED = 110;
    private static final int MAX_HEALTH = 200;
    private static final int ATTACK_RANGE = 120;
    private static final int ATTACK_POWER = 10;
    private static final float ATTACK_COOLDOWN = 0.6f;

    public AlienSoldierUnit(ILevel level) {
        super(Assets.getInstance().getUnits().bugAnimation, level, UNIT_WIDTH, UNIT_HEIGHT,
                DEFAULT_SPEED, MAX_HEALTH, ATTACK_RANGE, ATTACK_POWER, ATTACK_COOLDOWN, ENEMY_DETECTION_RANGE);
    }

    @Override
    public boolean isSupport() {
        return false;
    }

    @Override
    public Projectile rangedAttack(IDamageable<? extends AbstractGameObject> target) {
        return new Bullet(level, this, target, ATTACK_POWER);
    }
}