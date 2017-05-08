package hr.fer.zemris.zavrsni.rts.search.impl;

import hr.fer.zemris.zavrsni.rts.search.problem.ISearchProblem;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MoveToAdjacentTileProblem implements ISearchProblem<MapTile> {

    private final MoveToTileProblem moveToTileProblem;
    private final ILevel level;

    private final Set<MapTile> adjacentTiles = new HashSet<>(4);

    public MoveToAdjacentTileProblem(MapTile startPosition, MapTile endPosition, ILevel level) {
        this.level = level;
        moveToTileProblem = new MoveToTileProblem(startPosition, endPosition, level);

        if (LevelUtils.canMoveNorth(level, endPosition)) {
            adjacentTiles.add(new MapTile(endPosition.x, endPosition.y + 1));
        }
        if (LevelUtils.canMoveSouth(level, endPosition)) {
            adjacentTiles.add(new MapTile(endPosition.x, endPosition.y - 1));
        }
        if (LevelUtils.canMoveEast(level, endPosition)) {
            adjacentTiles.add(new MapTile(endPosition.x + 1, endPosition.y));
        }
        if (LevelUtils.canMoveWest(level, endPosition)) {
            adjacentTiles.add(new MapTile(endPosition.x - 1, endPosition.y));
        }
    }

    @Override
    public MapTile getStartState() {
        return moveToTileProblem.getStartState();
    }

    @Override
    public void setStartState(MapTile startState) {
        moveToTileProblem.setStartState(startState);

        if (!adjacentTiles.contains(startState)) return;

        MapTile goalTile = moveToTileProblem.getGoalState();
        if (level.getTileModifier(goalTile.x, goalTile.y) > 0) {
            adjacentTiles.clear();
        }
    }

    @Override
    public MapTile getGoalState() {
        return moveToTileProblem.getGoalState();
    }

    @Override
    public boolean isGoalState(MapTile state) {
        return moveToTileProblem.isGoalState(state) || adjacentTiles.contains(state);

    }

    @Override
    public List<Successor<MapTile>> getSuccessors(MapTile state) {
        return moveToTileProblem.getSuccessors(state);
    }

    public float getCachedModifier(MapTile position) {
        return moveToTileProblem.getCachedModifier(position);
    }
}
