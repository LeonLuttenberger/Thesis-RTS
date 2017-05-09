package hr.fer.zemris.zavrsni.rts.objects.units;

import com.badlogic.gdx.math.Vector2;
import hr.fer.zemris.zavrsni.rts.IUpdatable;
import hr.fer.zemris.zavrsni.rts.search.impl.MapTile;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

import java.util.ArrayList;
import java.util.List;

public class Squad implements IUpdatable {

    private static final float COHESION_WEIGHT = 1/100f * 100;
    private static final float ALIGNMENT_WEIGHT = 1/5f * 100;
    private static final float GOAL_WEIGHT = 1/100f * 100;
    private static final float SQUAD_SEPARATION_WEIGHT = 1/25f * 100;
    private static final float TERRAIN_SEPARATION_WEIGHT = 1/10f * 100;

    private static final int DETECTION_LIMIT_INCREASE = 2;

    private final List<Unit> squadMembers;
    private final ILevel level;

    private Unit squadLeader;

    public Squad(List<Unit> squadMembers, ILevel level) {
        this.squadMembers = squadMembers;
        this.level = level;
    }

    @Override
    public void update(float deltaTime) {
        squadLeader.updateSearchAgent();
        if (squadLeader.isSearchStopped()) {
            for (Unit squadMember : squadMembers) {
                squadMember.getVelocity().setLength(0);
            }
            return;
        }

        if (squadMembers.size() > 1) {
            for (Unit member : squadMembers) {
                if (member != squadLeader) {
                    applyGoal(member, squadLeader.getCurrentGoal());
                }

                applyCohesion(member);
                applyAlignment(member);
                applySquadSeparation(member);
                applyTerrainSeparation(member);
            }
        }
    }

    private void applyCohesion(Unit unit) {
        float dx = 0, dy = 0;

        for (Unit squadMember : squadMembers) {
            if (squadMember == unit) continue;

            dx += squadMember.getCenterX();
            dy += squadMember.getCenterY();
        }

        dx = dx / (squadMembers.size() - 1);
        dy = dy / (squadMembers.size() - 1);

        unit.getVelocity().x += (dx - unit.getCenterX()) * COHESION_WEIGHT;
        unit.getVelocity().y += (dy - unit.getCenterY()) * COHESION_WEIGHT;
    }

    private void applyAlignment(Unit unit) {
        float dx = 0, dy = 0;

        for (Unit squadMember : squadMembers) {
            if (squadMember == unit) continue;

            float angle = squadMember.getVelocity().angleRad();
            dx += Math.cos(angle);
            dy += Math.sin(angle);
        }

        dx = dx / (squadMembers.size() - 1);
        dy = dy / (squadMembers.size() - 1);

        float angleRad = unit.getVelocity().angleRad();
        unit.getVelocity().x += (dx - Math.cos(angleRad)) * ALIGNMENT_WEIGHT;
        unit.getVelocity().y += (dy - Math.sin(angleRad)) * ALIGNMENT_WEIGHT;
    }

    private void applyGoal(Unit unit, Vector2 squadGoal) {
        unit.getVelocity().x = (squadGoal.x - unit.getCenterX()) * GOAL_WEIGHT;
        unit.getVelocity().y = (squadGoal.y - unit.getCenterY()) * GOAL_WEIGHT;
    }

    private void applySquadSeparation(Unit unit) {
        float dx = 0, dy = 0;

        for (Unit squadMember : squadMembers) {
            if (unit == squadMember) continue;

            float unitRadius = Math.max(unit.getDimension().x, unit.getDimension().y) / 2f;
            float otherUnitRadius = Math.max(squadMember.getDimension().x, squadMember.getDimension().y) / 2f;

            float detectionLimit = unitRadius + otherUnitRadius + DETECTION_LIMIT_INCREASE;
            float distance = distance(
                    unit.getCenterX(), unit.getCenterY(),
                    squadMember.getCenterX(), squadMember.getCenterY());

            if (distance <= detectionLimit) {
                dx -= (squadMember.getCenterX() - unit.getCenterX());
                dy -= (squadMember.getCenterY() - unit.getCenterY());
            }
        }

        unit.getVelocity().x += dx * SQUAD_SEPARATION_WEIGHT;
        unit.getVelocity().y += dy * SQUAD_SEPARATION_WEIGHT;
    }

    private void applyTerrainSeparation(Unit unit) {
        float dx = 0, dy = 0;

        int currentTileX = (int) (unit.getCenterX() / level.getTileWidth());
        int currentTileY = (int) (unit.getCenterY() / level.getTileHeight());

        for (MapTile tile : adjacentTiles(currentTileX, currentTileY)) {

            if (level.getTileModifier(tile.x, tile.y) <= 0) {
                float unitRadius = Math.max(unit.getDimension().x, unit.getDimension().y) / 2f;
                float tileRadius = Math.max(level.getTileWidth(), level.getTileHeight()) / 2f;

                float detectionLimit = unitRadius + tileRadius;
                float adjacentTileX = tile.x * level.getTileWidth() + level.getTileWidth() / 2f;
                float adjacentTileY = tile.y * level.getTileHeight() + level.getTileHeight() / 2f;
                float distance = distance(
                        unit.getCenterX(), unit.getCenterY(),
                        adjacentTileX, adjacentTileY
                );

                if (distance <= detectionLimit) {
                    dx -= adjacentTileX - unit.getCenterX();
                    dy -= adjacentTileY - unit.getCenterY();
                }
            }
        }

        unit.getVelocity().x += dx * TERRAIN_SEPARATION_WEIGHT;
        unit.getVelocity().y += dy * TERRAIN_SEPARATION_WEIGHT;
    }

    private List<MapTile> adjacentTiles(int x, int y) {
        List<MapTile> adjacentTiles = new ArrayList<>(4);

        adjacentTiles.add(new MapTile(x + 1, y));
        adjacentTiles.add(new MapTile(x, y + 1));
        adjacentTiles.add(new MapTile(x - 1, y));
        adjacentTiles.add(new MapTile(x, y - 1));

        return adjacentTiles;
    }

    private static float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public void sendToLocation(float x, float y) {
        stopSearch();

        Unit closestUnit = null;
        float minDistance = Float.POSITIVE_INFINITY;
        for (Unit unit : squadMembers) {
            float distance = distance(unit.getCenterX(), unit.getCenterY(), x, y);
            if (distance < minDistance) {
                closestUnit = unit;
                minDistance = distance;
            }
        }

        squadLeader = closestUnit;
        squadLeader.sendToDestination(x, y);
    }

    public boolean isSearchStopped() {
        return squadLeader.isSearchStopped();
    }

    public void stopSearch() {
        if (squadLeader != null) {
            squadLeader.stopSearch();
        }

        for (Unit unit : squadMembers) {
            unit.getVelocity().setLength(0);
        }
    }
}
