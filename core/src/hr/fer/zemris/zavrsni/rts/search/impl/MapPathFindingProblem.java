package hr.fer.zemris.zavrsni.rts.search.impl;

import hr.fer.zemris.zavrsni.rts.search.ISearchProblem;
import hr.fer.zemris.zavrsni.rts.world.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class MapPathFindingProblem implements ISearchProblem<MapPosition> {

    private final MapPosition startPosition;
    private final MapPosition endPosition;
    private final Level level;

    public MapPathFindingProblem(MapPosition startPosition, MapPosition endPosition, Level level) {
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
        List<Successor<MapPosition>> successors = new ArrayList<>();

        if (state.x > 0) {
            addNewState(successors, state, new MapPosition(state.x - 1, state.y), Directions.WEST);
        }
        if (state.x < level.getWidth() - 1) {
            addNewState(successors, state, new MapPosition(state.x + 1, state.y), Directions.EAST);
        }
        if (state.y > 0) {
            addNewState(successors, state, new MapPosition(state.x, state.y - 1), Directions.SOUTH);
        }
        if (state.y < level.getHeight() - 1) {
            addNewState(successors, state, new MapPosition(state.x, state.y + 1), Directions.NORTH);
        }

        if (state.x > 0 && state.y > 0) {
            addNewState(successors, state, new MapPosition(state.x - 1, state.y - 1), Directions.SOUTH_WEST);
        }
        if (state.x < level.getWidth() - 1 && state.y > 0) {
            addNewState(successors, state, new MapPosition(state.x + 1, state.y - 1), Directions.SOUTH_EAST);
        }
        if (state.x > 0 && state.y < level.getHeight() - 1) {
            addNewState(successors, state, new MapPosition(state.x - 1, state.y + 1), Directions.NORTH_WEST);
        }
        if (state.x < level.getWidth() - 1 && state.y < level.getHeight() - 1) {
            addNewState(successors, state, new MapPosition(state.x + 1, state.y + 1), Directions.NORTH_EAST);
        }

        return successors;
    }

    private void addNewState(List<Successor<MapPosition>> successors, MapPosition pos, MapPosition newPos,
                             Directions direction) {
        if (level.getTileModifier(newPos.x, newPos.y) == 0) {
            return;
        }

        double distance = distance(pos, newPos);
        double stepCost = distance *
                        (1 / level.getTileModifier(pos.x, pos.y) +
                        1 / level.getTileModifier(newPos.x, newPos.y)) / 2;

        successors.add(new Successor<>(newPos, direction.getDirection(), stepCost));
    }

    private double distance(MapPosition pos1, MapPosition pos2) {
        return sqrt(pow(pos2.x - pos1.x, 2) + pow(pos2.y - pos1.y, 2));
    }
}
