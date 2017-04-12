package hr.fer.zemris.zavrsni.rts.search;

import java.util.List;

public interface ISearchProblem<T> {

    T getStartState();

    T getGoalState();

    boolean isGoalState(T state);

    List<Successor<T>> getSuccessors(T state);

    class Successor<T> {
        public final T state;
        public final Directions action;
        public final double stepCost;

        public Successor(T state, Directions action, double stepCost) {
            this.state = state;
            this.action = action;
            this.stepCost = stepCost;
        }
    }
}
