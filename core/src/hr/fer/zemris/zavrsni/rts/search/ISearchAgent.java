package hr.fer.zemris.zavrsni.rts.search;

public interface ISearchAgent<T> {

    void pathfind(T startState, T goalState);

    T update(T currentState);

    void stopSearch();
}
