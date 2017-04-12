package hr.fer.zemris.zavrsni.rts.search.impl;

import hr.fer.zemris.zavrsni.rts.search.Directions;
import hr.fer.zemris.zavrsni.rts.search.ISearchProblem;
import hr.fer.zemris.zavrsni.rts.world.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapPathfindingProblem implements ISearchProblem<MapPosition> {

    private final MapPosition startPosition;
    private final MapPosition endPosition;
    private final Level level;

    public MapPathfindingProblem(MapPosition startPosition, MapPosition endPosition, Level level) {
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
            addNewState(successors, state, new MapPosition(state.x, state.y - 1), Directions.NORTH);
        }
        if (state.y < level.getHeight() - 1) {
            addNewState(successors, state, new MapPosition(state.x, state.y + 1), Directions.SOUTH);
        }

        if (state.x > 0 && state.y > 0) {
            addNewState(successors, state, new MapPosition(state.x - 1, state.y - 1), Directions.NORTH_EAST);
        }
        if (state.x < level.getWidth() - 1 && state.y > 0) {
            addNewState(successors, state, new MapPosition(state.x + 1, state.y - 1), Directions.NORTH_WEST);
        }
        if (state.x > 0 && state.y < level.getHeight() - 1) {
            addNewState(successors, state, new MapPosition(state.x - 1, state.y + 1), Directions.SOUTH_EAST);
        }
        if (state.x < level.getWidth() - 1 && state.y < level.getHeight() - 1) {
            addNewState(successors, state, new MapPosition(state.x + 1, state.y + 1), Directions.SOUTH_WEST);
        }

        return successors;
    }

    private void addNewState(List<Successor<MapPosition>> successors, MapPosition pos, MapPosition newPos,
                             Directions direction) {
        double stepCost = (level.getTileModifier(pos.x, pos.y) + level.getTileModifier(newPos.x, newPos.y)) / 2;

        successors.add(new Successor<>(newPos, direction, stepCost));
    }
}
