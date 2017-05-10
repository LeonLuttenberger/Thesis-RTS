package hr.fer.zemris.zavrsni.rts.pathfinding.problem;

import java.util.List;

public interface ISearchProblem<T> {

    T getStartState();

    void setStartState(T startState);

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
