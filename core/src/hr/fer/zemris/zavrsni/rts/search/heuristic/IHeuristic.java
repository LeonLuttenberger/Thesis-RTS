package hr.fer.zemris.zavrsni.rts.search.heuristic;

import hr.fer.zemris.zavrsni.rts.search.problem.ISearchProblem;

public interface IHeuristic<T> {

    IHeuristic<?> NULL_HEURISTIC = (IHeuristic<Object>) (state, problem) -> 0;

    double calculateHeuristic(T state, ISearchProblem<T> problem);
}
