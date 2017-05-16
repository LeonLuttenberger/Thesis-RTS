package hr.fer.zemris.zavrsni.rts.objects.units;

import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.pathfinding.impl.MapTile;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

import java.util.ArrayList;
import java.util.List;

import static hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject.distanceBetween;

public final class MovementUtility {

    static final float COHESION_WEIGHT = 0.5f;
    static final float ALIGNMENT_WEIGHT = 10;
    static final float GOAL_WEIGHT = 0.5f;
    static final float SQUAD_SEPARATION_WEIGHT = 1.5f;
    static final float ENEMY_SEPARATION_WEIGHT = 20;
    static final float TERRAIN_SEPARATION_WEIGHT = 5;

    static final float SQUADMATE_DETECTION_LIMIT_INCREASE = 2;
    static final float SUPPORT_ENEMY_RANGE_INCREASE = 10;

    public static void applyFriendlySeparation(Unit unit, List<? extends Unit> friendlies) {
        float dx = 0, dy = 0;

        for (Unit otherUnit : friendlies) {
            if (unit == otherUnit) continue;

            float unitRadius = Math.max(unit.dimension.x, unit.dimension.y) / 2f;
            float otherUnitRadius = Math.max(otherUnit.dimension.x, otherUnit.dimension.y) / 2f;

            float detectionLimit = unitRadius + otherUnitRadius + SQUADMATE_DETECTION_LIMIT_INCREASE;
            float distance = distanceBetween(unit, otherUnit);
            if (distance <= detectionLimit) {
                dx -= (otherUnit.getCenterX() - unit.getCenterX());
                dy -= (otherUnit.getCenterY() - unit.getCenterY());
            }
        }

        unit.adjustDirection(
                dx * SQUAD_SEPARATION_WEIGHT,
                dy * SQUAD_SEPARATION_WEIGHT
        );
    }

    public static void applyEnemySeparation(Unit unit, List<? extends Unit> enemies) {
        float dx = 0, dy = 0;

        for (Unit enemy : enemies) {

            float detectionLimit;
            if (unit.isSupport()) {
                detectionLimit = enemy.getAttackRange() + SUPPORT_ENEMY_RANGE_INCREASE;
            } else {
                detectionLimit = unit.getAttackRange();
            }

            float distance = distanceBetween(unit, enemy);
            if (distance < detectionLimit) {
                dx -= enemy.getCenterX() - unit.getCenterX();
                dy -= enemy.getCenterY() - unit.getCenterY();
            }
        }

        unit.adjustDirection(
                dx * ENEMY_SEPARATION_WEIGHT,
                dy * ENEMY_SEPARATION_WEIGHT
        );
    }

    public static void applyTerrainSeparation(Unit unit, ILevel level) {
        float dx = 0, dy = 0;

        int currentTileX = (int) (unit.getCenterX() / level.getTileWidth());
        int currentTileY = (int) (unit.getCenterY() / level.getTileHeight());

        for (MapTile tile : adjacentTiles(currentTileX, currentTileY)) {

            if (level.getTileModifier(tile.x, tile.y) <= 0) {
                float unitRadius = Math.max(unit.dimension.x, unit.dimension.y) / 2f;
                float tileRadius = Math.max(level.getTileWidth(), level.getTileHeight()) / 2f;

                float detectionLimit = unitRadius + tileRadius;
                float adjacentTileX = tile.x * level.getTileWidth() + level.getTileWidth() / 2f;
                float adjacentTileY = tile.y * level.getTileHeight() + level.getTileHeight() / 2f;
                float distance = distanceBetween(unit, adjacentTileX, adjacentTileY);

                if (distance <= detectionLimit) {
                    dx -= adjacentTileX - unit.getCenterX();
                    dy -= adjacentTileY - unit.getCenterY();
                }
            }
        }

        unit.adjustDirection(
                dx * TERRAIN_SEPARATION_WEIGHT,
                dy * TERRAIN_SEPARATION_WEIGHT
        );
    }

    private static List<MapTile> adjacentTiles(int x, int y) {
        List<MapTile> adjacentTiles = new ArrayList<>(4);

        adjacentTiles.add(new MapTile(x + 1, y));
        adjacentTiles.add(new MapTile(x, y + 1));
        adjacentTiles.add(new MapTile(x - 1, y));
        adjacentTiles.add(new MapTile(x, y - 1));

        return adjacentTiles;
    }

    public static <T extends Unit> T closestUnitInRange(AbstractGameObject object, List<T> unitsToCheck, float range) {
        return closestUnitInRange(object.getCenterX(), object.getCenterY(), unitsToCheck, range);
    }

    public static <T extends Unit> T closestUnitInRange(float x, float y, List<T> units, float range) {
        T closestUnit = null;
        float minDistance = Float.POSITIVE_INFINITY;

        for (T otherUnit : units) {
            float distance = distanceBetween(otherUnit, x, y);
            if (distance < range && (closestUnit == null || distance < minDistance)) {
                minDistance = distance;
                closestUnit = otherUnit;
            }
        }

        return closestUnit;
    }

    private MovementUtility() {}
}
