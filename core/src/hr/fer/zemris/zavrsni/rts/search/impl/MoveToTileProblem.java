package hr.fer.zemris.zavrsni.rts.search.impl;

import hr.fer.zemris.zavrsni.rts.search.problem.ISearchProblem;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class MoveToTileProblem implements ISearchProblem<MapTile> {

    private final MapTile startPosition;
    private final MapTile endPosition;
    private final ILevel level;

    private final Map<MapTile, Float> modifierCache = new HashMap<>();

    public MoveToTileProblem(MapTile startPosition, MapTile endPosition, ILevel level) {
        this.startPosition = Objects.requireNonNull(startPosition, "Start position cannot be null.");
        this.endPosition = Objects.requireNonNull(endPosition, "End position cannot be null.");
        this.level = Objects.requireNonNull(level, "Level cannot be null.");

        modifierCache.put(startPosition, level.getTileModifier(startPosition.x, startPosition.y));
    }

    @Override
    public MapTile getStartState() {
        return startPosition;
    }

    @Override
    public MapTile getGoalState() {
        return endPosition;
    }

    @Override
    public boolean isGoalState(MapTile state) {
        Objects.requireNonNull(state, "Goal state cannot be null.");
        return endPosition.equals(state);
    }

    @Override
    public List<Successor<MapTile>> getSuccessors(MapTile state) {
        List<Successor<MapTile>> successors = new ArrayList<>();

        boolean canMoveNorth = canMoveNorth(state);
        boolean canMoveSouth = canMoveSouth(state);
        boolean canMoveEast = canMoveEast(state);
        boolean canMoveWest = canMoveWest(state);

        if (canMoveNorth) {
            addNewState(successors, state, state.x, state.y + 1);
        }
        if (canMoveSouth) {
            addNewState(successors, state, state.x, state.y - 1);
        }
        if (canMoveEast) {
            addNewState(successors, state, state.x + 1, state.y);
        }
        if (canMoveWest) {
            addNewState(successors, state, state.x - 1, state.y);
        }

        if (canMoveNorth && canMoveEast) {
            addNewState(successors, state, state.x + 1, state.y + 1);
        }
        if (canMoveNorth && canMoveWest) {
            addNewState(successors, state, state.x - 1, state.y + 1);
        }
        if (canMoveSouth && canMoveEast) {
            addNewState(successors, state, state.x + 1, state.y - 1);
        }
        if (canMoveSouth && canMoveWest) {
            addNewState(successors, state, state.x - 1, state.y - 1);
        }

        for (Successor<MapTile> successor : successors) {
            modifierCache.put(successor.state, level.getTileModifier(successor.state.x, successor.state.y));
        }

        return successors;
    }

    private void addNewState(List<Successor<MapTile>> successors, MapTile pos, int newTileX, int newTileY) {
        MapTile newPos = new MapTile(newTileX, newTileY);

        double distance = distance(pos, newPos);
        double stepCost = distance *
                        (1 / level.getTileModifier(pos.x, pos.y) +
                        1 / level.getTileModifier(newPos.x, newPos.y)) / 2;

        successors.add(new Successor<>(newPos, stepCost));
    }

    private double distance(MapTile pos1, MapTile pos2) {
        return sqrt(pow(pos2.x - pos1.x, 2) + pow(pos2.y - pos1.y, 2));
    }

    private boolean canMoveNorth(MapTile position) {
        if (position.y >= level.getHeight() - 1) return false;
        if (level.getTileModifier(position.x, position.y + 1) <= 0) return false;
        return true;
    }

    private boolean canMoveSouth(MapTile position) {
        if (position.y <= 0) return false;
        if (level.getTileModifier(position.x, position.y - 1) <= 0) return false;
        return true;
    }

    private boolean canMoveEast(MapTile position) {
        if (position.x >= level.getWidth()) return false;
        if (level.getTileModifier(position.x + 1, position.y) <= 0) return false;
        return true;
    }

    private boolean canMoveWest(MapTile position) {
        if (position.x <= 0) return false;
        if (level.getTileModifier(position.x - 1, position.y) <= 0) return false;
        return true;
    }

    public float getCachedModifier(MapTile position) {
        return modifierCache.get(position);
    }
}
