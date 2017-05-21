package hr.fer.zemris.zavrsni.rts.pathfinding.impl.problem;

import hr.fer.zemris.zavrsni.rts.common.ITiledMap;
import hr.fer.zemris.zavrsni.rts.common.LevelUtils;
import hr.fer.zemris.zavrsni.rts.common.MapTile;
import hr.fer.zemris.zavrsni.rts.pathfinding.problem.IModifierCachingProblem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MoveToAdjacentTileProblem implements IModifierCachingProblem<MapTile> {

    private final IModifierCachingProblem<MapTile> problem;
    private final ITiledMap level;

    private final Set<MapTile> adjacentTiles = new HashSet<>(4);

    public MoveToAdjacentTileProblem(ITiledMap level, IModifierCachingProblem<MapTile> problem) {
        this.level = level;
        this.problem = problem;

        MapTile endPosition = problem.getGoalState();

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
        return problem.getStartState();
    }

    @Override
    public void setStartState(MapTile startState) {
        problem.setStartState(startState);

        if (!adjacentTiles.contains(startState)) return;

        MapTile goalTile = problem.getGoalState();
        if (level.getTileModifier(goalTile.x, goalTile.y) > 0) {
            adjacentTiles.clear();
        }
    }

    @Override
    public MapTile getGoalState() {
        return problem.getGoalState();
    }

    @Override
    public boolean isGoalState(MapTile state) {
        return problem.isGoalState(state) || adjacentTiles.contains(state);
    }

    @Override
    public List<Successor<MapTile>> getSuccessors(MapTile state) {
        return problem.getSuccessors(state);
    }

    @Override
    public float getCachedModifier(MapTile position) {
        return problem.getCachedModifier(position);
    }
}
