package hr.fer.zemris.zavrsni.rts.search.algorithms;

import hr.fer.zemris.zavrsni.rts.search.SearchResult;
import hr.fer.zemris.zavrsni.rts.search.heuristic.IHeuristic;
import hr.fer.zemris.zavrsni.rts.search.problem.ISearchProblem;

public interface ISearchAlgorithm<T> {

    SearchResult<T> search(ISearchProblem<T> problem, IHeuristic<T> heuristic);
}
