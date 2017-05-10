package hr.fer.zemris.zavrsni.rts.objects.units;

import com.badlogic.gdx.math.Vector2;
import hr.fer.zemris.zavrsni.rts.IUpdatable;
import hr.fer.zemris.zavrsni.rts.objects.units.hostile.HostileUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.player.PlayerUnit;
import hr.fer.zemris.zavrsni.rts.search.impl.MapTile;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject.distanceBetween;

public class Squad implements IUpdatable {

    private static final float COHESION_WEIGHT = 1;
    private static final float ALIGNMENT_WEIGHT = 20;
    private static final float GOAL_WEIGHT = 1;
    private static final float SQUAD_SEPARATION_WEIGHT = 4;
    private static final float ENEMY_SEPARATION_WEIGHT = 100;
    private static final float TERRAIN_SEPARATION_WEIGHT = 10;

    private static final float SQUADMATE_DETECTION_LIMIT_INCREASE = 2;
    private static final float SUPPORT_ENEMY_RANGE_INCREASE = 10;

    private final Consumer<PlayerUnit> functionApplyCohesion = this::applyCohesion;
    private final Consumer<PlayerUnit> functionApplyAlignment = this::applyAlignment;
    private final Consumer<PlayerUnit> functionApplySquadSeparation = this::applySquadSeparation;
    private final Consumer<PlayerUnit> functionApplyEnemySeparation = this::applyEnemySeparation;
    private final Consumer<PlayerUnit> functionApplyTerrainSeparation = this::applyTerrainSeparation;

    private final List<PlayerUnit> squadMembers;
    private final ILevel level;

    private PlayerUnit squadLeader;

    public Squad(List<PlayerUnit> squadMembers, ILevel level) {
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

        if (squadMembers.size() <= 1) return;

        for (PlayerUnit squadMember : squadMembers) {
            if (squadMember == squadLeader) continue;
            applyGoal(squadMember, squadLeader.getCurrentGoal());
        }
        squadMembers.forEach(functionApplyCohesion);
        squadMembers.forEach(functionApplyAlignment);
        squadMembers.forEach(functionApplySquadSeparation);
        squadMembers.forEach(functionApplyEnemySeparation);
        squadMembers.forEach(functionApplyTerrainSeparation);
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

            float detectionLimit = unitRadius + otherUnitRadius + SQUADMATE_DETECTION_LIMIT_INCREASE;
            float distance = distanceBetween(unit, squadMember);
            if (distance <= detectionLimit) {
                dx -= (squadMember.getCenterX() - unit.getCenterX());
                dy -= (squadMember.getCenterY() - unit.getCenterY());
            }
        }

        unit.getVelocity().x += dx * SQUAD_SEPARATION_WEIGHT;
        unit.getVelocity().y += dy * SQUAD_SEPARATION_WEIGHT;
    }

    private void applyEnemySeparation(Unit unit) {
        float dx = 0, dy = 0;

        for (HostileUnit enemy : level.getHostileUnits()) {

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

        unit.getVelocity().x += dx * ENEMY_SEPARATION_WEIGHT;
        unit.getVelocity().y += dy * ENEMY_SEPARATION_WEIGHT;
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
                float distance = distanceBetween(unit, adjacentTileX, adjacentTileY);

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

    public void sendToLocation(float x, float y) {
        stopSearch();

        PlayerUnit closestUnit = null;
        float minDistance = Float.POSITIVE_INFINITY;
        for (PlayerUnit unit : squadMembers) {
            float distance = distanceBetween(unit, x, y);
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
