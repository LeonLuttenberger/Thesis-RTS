package hr.fer.zemris.zavrsni.rts.pathfinding.heuristic;

import hr.fer.zemris.zavrsni.rts.pathfinding.problem.CachingProblem;
import hr.fer.zemris.zavrsni.rts.pathfinding.problem.ISearchProblem;

import java.util.OptionalDouble;

public class CachingProblemHeuristic<T> implements IHeuristic<T> {

    private final IHeuristic<T> heuristic;

    public CachingProblemHeuristic(IHeuristic<T> heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public double calculateHeuristic(T state, ISearchProblem<T> problem) {
        if (problem instanceof CachingProblem) {
            CachingProblem<T, ISearchProblem<T>> cachingProblem = (CachingProblem<T, ISearchProblem<T>>) problem;

            OptionalDouble heuristicValue = cachingProblem.getCachedHeuristic(state);
            if (heuristicValue.isPresent()) {
                return heuristicValue.getAsDouble();
            } else {
                double calculatedHeuristicValue = this.heuristic.calculateHeuristic(state, problem);
                cachingProblem.cacheHeuristic(state, calculatedHeuristicValue);
                return calculatedHeuristicValue;
            }
        }

        return heuristic.calculateHeuristic(state, problem);
    }
}
