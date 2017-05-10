package hr.fer.zemris.zavrsni.rts.pathfinding.problem;

import java.util.List;

public class LimitedExpansionProblem<T, S extends ISearchProblem<T>> implements ISearchProblem<T> {

    private final S problem;
    private final int maxStatesToExpand;

    private int expandedStates = 0;

    public LimitedExpansionProblem(S problem, int maxStatesToExpand) {
        this.problem = problem;
        this.maxStatesToExpand = maxStatesToExpand;
    }

    @Override
    public T getStartState() {
        return problem.getStartState();
    }

    @Override
    public void setStartState(T startState) {
        problem.setStartState(startState);
        expandedStates = 0;
    }

    @Override
    public T getGoalState() {
        return problem.getGoalState();
    }

    @Override
    public boolean isGoalState(T state) {
        return problem.isGoalState(state) || expandedStates >= maxStatesToExpand;
    }

    @Override
    public List<Successor<T>> getSuccessors(T state) {
        expandedStates++;
        return problem.getSuccessors(state);
    }

    public S getProblem() {
        return problem;
    }
}
