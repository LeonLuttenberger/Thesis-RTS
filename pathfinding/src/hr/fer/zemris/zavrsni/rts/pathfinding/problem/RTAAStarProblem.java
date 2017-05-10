package hr.fer.zemris.zavrsni.rts.pathfinding.problem;

public class RTAAStarProblem<T, S extends ISearchProblem<T>> extends CachingProblem<T, LimitedExpansionProblem<T, S>> {

    private T newStartState = null;

    public RTAAStarProblem(S problem, int lookahead) {
        super(new LimitedExpansionProblem<>(problem, lookahead));
    }

    @Override
    public T getStartState() {
        if (newStartState == null) {
            return super.getStartState();
        } else {
            return newStartState;
        }
    }

    @Override
    public void setStartState(T startState) {
        super.setStartState(startState);
    }
}
