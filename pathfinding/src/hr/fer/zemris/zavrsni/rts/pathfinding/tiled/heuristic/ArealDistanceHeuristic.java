package hr.fer.zemris.zavrsni.rts.pathfinding.tiled.heuristic;

import hr.fer.zemris.zavrsni.rts.pathfinding.heuristic.IHeuristic;
import hr.fer.zemris.zavrsni.rts.pathfinding.problem.ISearchProblem;
import hr.fer.zemris.zavrsni.rts.pathfinding.tiled.map.MapTile;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class ArealDistanceHeuristic implements IHeuristic<MapTile> {

    @Override
    public double calculateHeuristic(MapTile state, ISearchProblem<MapTile> problem) {
        MapTile goalState = problem.getGoalState();
        return sqrt(pow(state.x - goalState.x, 2) + pow(state.y - goalState.y, 2));
    }
}
