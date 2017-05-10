package hr.fer.zemris.zavrsni.rts.objects.units.player;

import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.resources.Resource;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public class WorkerUnit extends PlayerUnit {

    private static final int UNIT_WIDTH = 24;
    private static final int UNIT_HEIGHT = 48;
    private static final float DEFAULT_SPEED = 200;
    private static final int MAX_HEALTH = 50;
    private static final float ATTACK_RANGE = 30;
    private static final int ATTACK_POWER = 1;
    private static final float ATTACK_COOLDOWN = 1;

    private Resource targetResource;

    public WorkerUnit(ILevel level) {
        super(Assets.getInstance().getUnits().workerAnimation, level, UNIT_WIDTH, UNIT_HEIGHT,
                DEFAULT_SPEED, MAX_HEALTH, ATTACK_RANGE, ATTACK_POWER, ATTACK_COOLDOWN);
    }

    @Override
    public void update(float deltaTime) {
        if (targetResource != null) {
            float dx = getCenterX() - targetResource.getCenterX();
            float dy = getCenterY() - targetResource.getCenterY();

            float horizontalDetectionLimit = (targetResource.getDimension().x + dimension.x) / 2 + 20;
            float verticalDetectionLimit = (targetResource.getDimension().y + dimension.y) / 2 + 20;

            if (Math.abs(dx) < horizontalDetectionLimit && Math.abs(dy) < verticalDetectionLimit) {
                gatherResource();
                velocity.setLength(0);
            } else if (isSearchStopped()) {
                velocity.set(dx, dy);
                velocity.setLength(level.getTerrainModifier(getCenterX(), getCenterY()));
            }
        }

        super.update(deltaTime);
    }

    private void gatherResource() {
        targetResource.removeHitPoints(1);
    }

    @Override
    public void sendToDestination(float x, float y) {
        int tileX = (int) (x / level.getTileWidth());
        int tileY = (int) (y / level.getTileHeight());

        AbstractGameObject objectOnTile = level.getObjectOnTile(tileX, tileY);
        if ((objectOnTile instanceof Resource)) {
            targetResource = (Resource) objectOnTile;
        }

        super.sendToDestination(x, y);
    }

    @Override
    public boolean isSupport() {
        return true;
    }
}
