package hr.fer.zemris.zavrsni.rts.search.impl;

import hr.fer.zemris.zavrsni.rts.search.ISearchProblem;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class MapPathFindingProblem implements ISearchProblem<MapPosition> {

    private final MapPosition startPosition;
    private final MapPosition endPosition;
    private final ILevel level;

    private final Map<MapPosition, Float> modifierCache = new HashMap<>();

    public MapPathFindingProblem(MapPosition startPosition, MapPosition endPosition, ILevel level) {
        this.startPosition = Objects.requireNonNull(startPosition, "Start position cannot be null.");
        this.endPosition = Objects.requireNonNull(endPosition, "End position cannot be null.");
        this.level = Objects.requireNonNull(level, "Level cannot be null.");
    }

    @Override
    public MapPosition getStartState() {
        return startPosition;
    }

    @Override
    public MapPosition getGoalState() {
        return endPosition;
    }

    @Override
    public boolean isGoalState(MapPosition state) {
        Objects.requireNonNull(state, "Goal state cannot be null.");
        return endPosition.equals(state);
    }

    @Override
    public List<Successor<MapPosition>> getSuccessors(MapPosition state) {
        modifierCache.put(state, level.getTileModifier(state.x, state.y));

        List<Successor<MapPosition>> successors = new ArrayList<>();

        if (state.x > 0) {
            // left
            addNewState(successors, state, new MapPosition(state.x - 1, state.y));
        }
        if (state.x < level.getWidth() - 1) {
            // right
            addNewState(successors, state, new MapPosition(state.x + 1, state.y));
        }
        if (state.y > 0) {
            // down
            addNewState(successors, state, new MapPosition(state.x, state.y - 1));
        }
        if (state.y < level.getHeight() - 1) {
            // up
            addNewState(successors, state, new MapPosition(state.x, state.y + 1));
        }

        if (state.x > 0 && state.y > 0 && level.getTileModifier(state.x - 1, state.y) > 0
                && level.getTileModifier(state.x, state.y - 1) > 0) {
            // down left
            addNewState(successors, state, new MapPosition(state.x - 1, state.y - 1));
        }
        if (state.x < level.getWidth() - 1 && state.y > 0 && level.getTileModifier(state.x + 1, state.y) > 0
                && level.getTileModifier(state.x, state.y - 1) > 0) {
            // down right
            addNewState(successors, state, new MapPosition(state.x + 1, state.y - 1));
        }
        if (state.x > 0 && state.y < level.getHeight() - 1 && level.getTileModifier(state.x - 1, state.y) > 0
                && level.getTileModifier(state.x, state.y + 1) > 0) {
            // up left
            addNewState(successors, state, new MapPosition(state.x - 1, state.y + 1));
        }
        if (state.x < level.getWidth() - 1 && state.y < level.getHeight() - 1 && level.getTileModifier(state.x + 1, state.y) > 0
                && level.getTileModifier(state.x, state.y + 1) > 0) {
            // up right
            addNewState(successors, state, new MapPosition(state.x + 1, state.y + 1));
        }

        return successors;
    }

    private void addNewState(List<Successor<MapPosition>> successors, MapPosition pos, MapPosition newPos) {
        if (level.getTileModifier(newPos.x, newPos.y) == 0) {
            return;
        }

        double distance = distance(pos, newPos);
        double stepCost = distance *
                        (1 / level.getTileModifier(pos.x, pos.y) +
                        1 / level.getTileModifier(newPos.x, newPos.y)) / 2;

        successors.add(new Successor<>(newPos, stepCost));
    }

    private double distance(MapPosition pos1, MapPosition pos2) {
        return sqrt(pow(pos2.x - pos1.x, 2) + pow(pos2.y - pos1.y, 2));
    }

    public float getCachedModifier(MapPosition position) {
        return modifierCache.get(position);
    }
}
