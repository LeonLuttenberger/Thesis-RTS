package hr.fer.zemris.zavrsni.rts.objects.units.player;

import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public class SoldierUnit extends PlayerUnit {

    private static final int UNIT_WIDTH = 48;
    private static final int UNIT_HEIGHT = 48;
    private static final float DEFAULT_SPEED = 200;
    private static final int MAX_HEALTH = 3000;
    private static final int ATTACK_RANGE = 50;
    private static final int ATTACK_POWER = 10;

    public SoldierUnit(ILevel level) {
        super(Assets.getInstance().getUnits().soldierAnimation, level, UNIT_WIDTH, UNIT_HEIGHT,
                DEFAULT_SPEED, MAX_HEALTH, ATTACK_RANGE, ATTACK_POWER);
    }

    @Override
    public boolean isSupport() {
        return false;
    }
}
