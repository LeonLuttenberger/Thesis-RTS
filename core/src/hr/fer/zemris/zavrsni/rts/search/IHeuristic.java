package hr.fer.zemris.zavrsni.rts.search;

public interface IHeuristic<T> {

    double calculateHeuristic(T state, ISearchProblem<T> problem);
}
