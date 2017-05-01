package hr.fer.zemris.zavrsni.rts.objects.units;

import hr.fer.zemris.zavrsni.rts.assets.Assets;

public class SimpleUnit extends Unit {

    public static final float DEFAULT_SPEED = 100 * 5;

    public SimpleUnit() {
        super(Assets.getInstance().getUnits().soldierAnimation, 48, 48);
    }

    @Override
    public float getMaxSpeed() {
        return DEFAULT_SPEED;
    }
}
