package hr.fer.zemris.zavrsni.rts.search.algorithms;

import com.badlogic.gdx.Gdx;
import hr.fer.zemris.zavrsni.rts.search.SearchNode;
import hr.fer.zemris.zavrsni.rts.search.SearchResult;
import hr.fer.zemris.zavrsni.rts.search.heuristic.IHeuristic;
import hr.fer.zemris.zavrsni.rts.search.problem.ISearchProblem;
import hr.fer.zemris.zavrsni.rts.search.problem.ISearchProblem.Successor;

import java.util.AbstractQueue;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractSearchAlgorithm<T> {

    private static final String TAG = AbstractSearchAlgorithm.class.getName();

    public abstract SearchResult<T> search(ISearchProblem<T> problem, IHeuristic<T> heuristic);

    protected final SearchResult<T> generalSearch(ISearchProblem<T> problem, AbstractQueue<SearchNode<T>> frontier,
                                             IHeuristic<T> heuristic) {
        T startState = problem.getStartState();
        SearchNode<T> searchNode = new SearchNode<>(
                startState, null, 0,
                heuristic.calculateHeuristic(startState, problem)
        );

        Set<SearchNode<T>> expanded = new HashSet<>();
        frontier.add(searchNode);

        while (!frontier.isEmpty()) {
            SearchNode<T> node = frontier.peek();
            if (problem.isGoalState(node.getState())) {
                Gdx.app.log(TAG, "Path found after " + expanded.size() + " expanded states.");
                return new SearchResult<>(node.backtrack(), expanded, frontier);
            }
            frontier.poll();

            if (expanded.contains(node)) {
                continue;
            }

            expanded.add(node);

            for (Successor<T> successor : problem.getSuccessors(node.getState())) {

                SearchNode<T> newNode = new SearchNode<>(successor.state, node, node.getCost() + successor.stepCost,
                        heuristic.calculateHeuristic(successor.state, problem));

                frontier.add(newNode);
            }
        }

        Gdx.app.log(TAG, "Path not found after " + expanded.size() + " expanded states.");
        return null;
    }
}
