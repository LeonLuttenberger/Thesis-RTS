package hr.fer.zemris.zavrsni.rts.search.algorithms;

import hr.fer.zemris.zavrsni.rts.search.IHeuristic;
import hr.fer.zemris.zavrsni.rts.search.ISearchProblem;
import hr.fer.zemris.zavrsni.rts.search.SearchNode;
import hr.fer.zemris.zavrsni.rts.search.SearchResult;

import java.util.PriorityQueue;

public class AStarSearch<T> extends AbstractSearchAlgorithm<T> {

    @Override
    public SearchResult<T> search(ISearchProblem<T> problem, IHeuristic<T> heuristic) {
        return generalSearch(problem, new PriorityQueue<>(SearchNode.BY_COST_AND_HEURISTIC), heuristic);
    }
}
