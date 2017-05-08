package hr.fer.zemris.zavrsni.rts.objects.units;

import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.resources.Resource;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public class WorkerUnit extends Unit {

    private static final int UNIT_WIDTH = 24;
    private static final int UNIT_HEIGHT = 48;
    private static final float DEFAULT_SPEED = 200;

    private Resource targetResource;

    public WorkerUnit(ILevel level) {
        super(Assets.getInstance().getUnits().workerAnimation, level, UNIT_WIDTH, UNIT_HEIGHT, DEFAULT_SPEED);
    }

    @Override
    public void update(float deltaTime) {
        if (targetResource != null) {
            float dx = Math.abs(getCenterX() - targetResource.getCenterX());
            float dy = Math.abs(getCenterY() - targetResource.getCenterY());

            if (dx < 3 * level.getTileWidth() / 4f && dy < 3 * level.getTileHeight() / 4f) {
                gatherResource();
            }
        }

        super.update(deltaTime);
    }

    private void gatherResource() {
        targetResource.removeDurability(1);
    }

    @Override
    public void sendToDestination(float x, float y) {
        int tileX = (int) (x / level.getTileWidth());
        int tileY = (int) (y / level.getTileHeight());

        AbstractGameObject objectOnTile = level.getObjectOnTile(tileX, tileY);
        if ((objectOnTile instanceof Resource)) {
            targetResource = (Resource) objectOnTile;
            System.out.println("I'll go get the resource now...");
        }

        super.sendToDestination(x, y);
    }
}
