package hr.fer.zemris.zavrsni.rts.pathfinding.algorithms;

import hr.fer.zemris.zavrsni.rts.pathfinding.SearchResult;
import hr.fer.zemris.zavrsni.rts.pathfinding.heuristic.IHeuristic;
import hr.fer.zemris.zavrsni.rts.pathfinding.problem.ISearchProblem;

public interface ISearchAlgorithm<T> {

    SearchResult<T> search(ISearchProblem<T> problem, IHeuristic<T> heuristic);
}
