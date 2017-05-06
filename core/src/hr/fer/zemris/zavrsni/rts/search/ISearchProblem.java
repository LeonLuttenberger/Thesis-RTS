package hr.fer.zemris.zavrsni.rts.search;

import java.util.List;

public interface ISearchProblem<T> {

    T getStartState();

    T getGoalState();

    boolean isGoalState(T state);

    List<Successor<T>> getSuccessors(T state);

    class Successor<T> {
        public final T state;
        public final double stepCost;

        public Successor(T state, double stepCost) {
            this.state = state;
            this.stepCost = stepCost;
        }
    }
}
