package hr.fer.zemris.zavrsni.rts.pathfinding.problem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

public class CachingProblem<T, S extends ISearchProblem<T>> implements ISearchProblem<T> {

    private final S problem;
    private final Map<T, Double> heuristicCache = new HashMap<>();

    public CachingProblem(S problem) {
        this.problem = problem;
    }

    @Override
    public T getStartState() {
        return problem.getStartState();
    }

    @Override
    public void setStartState(T startState) {
        problem.setStartState(startState);
    }

    @Override
    public T getGoalState() {
        return problem.getGoalState();
    }

    @Override
    public boolean isGoalState(T state) {
        return problem.isGoalState(state);
    }

    @Override
    public List<Successor<T>> getSuccessors(T state) {
        return problem.getSuccessors(state);
    }

    public OptionalDouble getCachedHeuristic(T state) {
        if (heuristicCache.containsKey(state)) {
            return OptionalDouble.of(heuristicCache.get(state));
        }

        return OptionalDouble.empty();
    }

    public void cacheHeuristic(T state, double heuristic) {
        heuristicCache.put(state, heuristic);
    }

    public S getProblem() {
        return problem;
    }
}
