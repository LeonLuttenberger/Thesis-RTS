package hr.fer.zemris.zavrsni.rts.objects.units;

import hr.fer.zemris.zavrsni.rts.assets.Assets;

public class SimpleUnit extends AbstractUnit {

    public static final float DEFAULT_SPEED = 100 * 5;

    public SimpleUnit() {
        super(Assets.getInstance().getUnits().soldierAnimation, 60, 60);
    }

    @Override
    public float getMaxSpeed() {
        return DEFAULT_SPEED;
    }
}
