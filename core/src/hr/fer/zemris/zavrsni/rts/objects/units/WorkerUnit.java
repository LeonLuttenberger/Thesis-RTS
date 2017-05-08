package hr.fer.zemris.zavrsni.rts.objects.units;

import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.resources.Resource;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public class WorkerUnit extends Unit {

    private static final int UNIT_WIDTH = 24;
    private static final int UNIT_HEIGHT = 48;
    private static final float DEFAULT_SPEED = 200;

    private Resource target;

    public WorkerUnit(ILevel level) {
        super(Assets.getInstance().getUnits().workerAnimation, level, UNIT_WIDTH, UNIT_HEIGHT, DEFAULT_SPEED);
    }

    @Override
    public void issueCommandTo(float x, float y) {
        int tileX = (int) (x / level.getTileWidth());
        int tileY = (int) (y / level.getTileHeight());

        AbstractGameObject objectOnTile = level.getObjectOnTile(tileX, tileY);
        if (!(objectOnTile instanceof Resource)) {
            super.issueCommandTo(x, y);
            return;
        }

        target = (Resource) objectOnTile;

        System.out.println("I'll go get the resource now...");
    }
}
