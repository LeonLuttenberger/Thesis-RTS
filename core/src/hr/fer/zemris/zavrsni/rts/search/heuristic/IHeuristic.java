package hr.fer.zemris.zavrsni.rts.search.heuristic;

import hr.fer.zemris.zavrsni.rts.search.problem.ISearchProblem;

public interface IHeuristic<T> {

    double calculateHeuristic(T state, ISearchProblem<T> problem);
}
