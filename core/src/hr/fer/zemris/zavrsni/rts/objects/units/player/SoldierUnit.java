package hr.fer.zemris.zavrsni.rts.objects.units.player;

import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.units.hostile.HostileUnit;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public class SoldierUnit extends PlayerUnit {

    private static final int UNIT_WIDTH = 48;
    private static final int UNIT_HEIGHT = 48;
    private static final float DEFAULT_SPEED = 200;
    private static final int MAX_HEALTH = 300;
    private static final int ATTACK_RANGE = 150;
    private static final int ATTACK_POWER = 10;
    private static final float ATTACK_COOLDOWN = 1;

    public SoldierUnit(ILevel level) {
        super(Assets.getInstance().getUnits().soldierAnimation, level, UNIT_WIDTH, UNIT_HEIGHT,
                DEFAULT_SPEED, MAX_HEALTH, ATTACK_RANGE, ATTACK_POWER, ATTACK_COOLDOWN);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        timeSinceLastAttack += deltaTime;

        if (timeSinceLastAttack > attackCooldown) {
            for (HostileUnit unit : level.getHostileUnits()) {
                float distance = AbstractGameObject.distanceBetween(this, unit);
                if (distance < attackRange) {
                    System.out.println("Attacking!");
                    unit.removeHitPoints(attackPower);
                    timeSinceLastAttack = 0;
                    break;
                }
            }
        }
    }

    @Override
    public boolean isSupport() {
        return false;
    }
}
