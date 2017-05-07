package hr.fer.zemris.zavrsni.rts.search;

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

    public void reset() {
        expandedStates = 0;
    }

    public S getProblem() {
        return problem;
    }
}
