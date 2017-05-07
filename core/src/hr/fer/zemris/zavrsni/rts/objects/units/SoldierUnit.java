package hr.fer.zemris.zavrsni.rts.objects.units;

import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public class SoldierUnit extends Unit {

    private static final int UNIT_WIDTH = 48;
    private static final int UNIT_HEIGHT = 48;
    private static final float DEFAULT_SPEED = 200;

    public SoldierUnit(ILevel level) {
        super(Assets.getInstance().getUnits().soldierAnimation, level, UNIT_WIDTH, UNIT_HEIGHT, DEFAULT_SPEED);
    }
}
