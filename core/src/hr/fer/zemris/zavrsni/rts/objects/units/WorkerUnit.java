package hr.fer.zemris.zavrsni.rts.objects.units;

import hr.fer.zemris.zavrsni.rts.assets.Assets;

public class WorkerUnit extends Unit {

    private static final int UNIT_WIDTH = 24;
    private static final int UNIT_HEIGHT = 48;
    private static final float DEFAULT_SPEED = 100 * 5;

    public WorkerUnit() {
        super(Assets.getInstance().getUnits().workerAnimation, UNIT_WIDTH, UNIT_HEIGHT);
    }

    @Override
    public float getMaxSpeed() {
        return DEFAULT_SPEED;
    }
}
