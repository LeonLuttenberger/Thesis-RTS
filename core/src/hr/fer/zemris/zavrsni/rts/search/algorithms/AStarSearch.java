package hr.fer.zemris.zavrsni.rts.search.algorithms;

import hr.fer.zemris.zavrsni.rts.search.SearchNode;
import hr.fer.zemris.zavrsni.rts.search.SearchResult;
import hr.fer.zemris.zavrsni.rts.search.heuristic.IHeuristic;
import hr.fer.zemris.zavrsni.rts.search.problem.ISearchProblem;

import java.util.PriorityQueue;

import static hr.fer.zemris.zavrsni.rts.search.algorithms.AlgorithmUtils.generalSearch;

public class AStarSearch<T> implements ISearchAlgorithm<T> {

    @Override
    public SearchResult<T> search(ISearchProblem<T> problem, IHeuristic<T> heuristic) {
        return generalSearch(problem, new PriorityQueue<>(SearchNode.BY_COST_AND_HEURISTIC), heuristic);
    }
}
