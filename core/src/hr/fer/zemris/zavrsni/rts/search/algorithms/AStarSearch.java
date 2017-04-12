package hr.fer.zemris.zavrsni.rts.search.algorithms;

import hr.fer.zemris.zavrsni.rts.search.IHeuristic;
import hr.fer.zemris.zavrsni.rts.search.ISearchProblem;
import hr.fer.zemris.zavrsni.rts.search.SearchNode;
import hr.fer.zemris.zavrsni.rts.search.Transition;

import java.util.List;
import java.util.PriorityQueue;

public class AStarSearch<T> extends AbstractSearchAlgorithm<T> {

    private final IHeuristic<T> heuristic;

    public AStarSearch(IHeuristic<T> heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public List<Transition> search(ISearchProblem<T> problem) {
        return generalSearch(problem, new PriorityQueue<>(SearchNode.BY_COST_AND_HEURISTIC), heuristic);
    }
}
