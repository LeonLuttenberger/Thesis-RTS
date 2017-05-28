package hr.fer.zemris.zavrsni.rts.objects.units.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.resources.Resource;
import hr.fer.zemris.zavrsni.rts.objects.units.IBuildableUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.PlayerUnit;

public class WorkerUnit extends PlayerUnit implements IBuildableUnit {

    private static final long serialVersionUID = -7973754481190223624L;

    private static final int UNIT_WIDTH = 16;
    private static final int UNIT_HEIGHT = 32;
    private static final float DEFAULT_SPEED = 100;
    private static final int MAX_HEALTH = 50;
    private static final float ATTACK_RANGE = 30;
    private static final int ATTACK_POWER = 1;
    private static final float ATTACK_COOLDOWN = 1;
    private static final int TRAINING_COST = 6000;

    private static final float RESOURCE_DETECTION_RANGE = 100;

    private Resource targetResource;

    public WorkerUnit(ILevel level) {
        super(level, UNIT_WIDTH, UNIT_HEIGHT, DEFAULT_SPEED, MAX_HEALTH, ATTACK_RANGE, ATTACK_POWER, ATTACK_COOLDOWN);
    }

    @Override
    public Animation<TextureRegion> loadAnimation() {
        return Assets.getInstance().getUnits().workerAnimation;
    }

    @Override
    public void update(float deltaTime) {
        if (targetResource != null) {
            float dx = targetResource.getCenterX() - getCenterX();
            float dy = targetResource.getCenterY() - getCenterY();

            float horizontalDetectionLimit = (targetResource.dimension.x + dimension.x) / 2 + 20;
            float verticalDetectionLimit = (targetResource.dimension.y + dimension.y) / 2 + 20;

            if (Math.abs(dx) < horizontalDetectionLimit && Math.abs(dy) < verticalDetectionLimit) {
                gatherResource();
                newVelocity.setLength(0);
            } else if (isSearchStopped()) {
                newVelocity.set(dx, dy);
                newVelocity.setLength(level.getTerrainModifier(getCenterX(), getCenterY()) * defaultSpeed);
            }
        }

        super.update(deltaTime);
    }

    private void gatherResource() {
        targetResource.removeHitPoints(1);

        if (targetResource.isDestroyed()) {

            // set new target
            Resource closestResource = null;
            float minDistance = Float.POSITIVE_INFINITY;

            for (Resource resource : level.getResources()) {
                float distance = AbstractGameObject.distanceBetween(this, resource);
                if (distance < RESOURCE_DETECTION_RANGE && distance < minDistance) {
                    closestResource = resource;
                    minDistance = distance;
                }
            }

            targetResource = closestResource;
        }
    }

    @Override
    public void sendToDestination(float x, float y) {
        targetResource = null;

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

    @Override
    public int getTrainingCost() {
        return TRAINING_COST;
    }
}
