package hr.fer.zemris.zavrsni.rts.pathfinding;

public interface ISearchAgent<T> {

    void pathfind(T startState, T goalState);

    T update(T currentState);

    void stopSearch();

    boolean isGoalState(T state);

    T getGoalState();
}
