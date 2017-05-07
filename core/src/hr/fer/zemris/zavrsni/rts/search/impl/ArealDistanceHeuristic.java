package hr.fer.zemris.zavrsni.rts.search.impl;

import hr.fer.zemris.zavrsni.rts.search.IHeuristic;
import hr.fer.zemris.zavrsni.rts.search.ISearchProblem;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class ArealDistanceHeuristic implements IHeuristic<MapPosition> {

    @Override
    public double calculateHeuristic(MapPosition state, ISearchProblem<MapPosition> problem) {
        MapPosition goalState = problem.getGoalState();
        return 5 * sqrt(pow(state.x - goalState.x, 2) + pow(state.y - goalState.y, 2));
    }
}
