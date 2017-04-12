package hr.fer.zemris.zavrsni.rts.search.algorithms;

import hr.fer.zemris.zavrsni.rts.search.Directions;
import hr.fer.zemris.zavrsni.rts.search.IHeuristic;
import hr.fer.zemris.zavrsni.rts.search.ISearchProblem;
import hr.fer.zemris.zavrsni.rts.search.ISearchProblem.Successor;
import hr.fer.zemris.zavrsni.rts.search.SearchNode;

import java.util.AbstractQueue;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractSearchAlgorithm<T> {

    public abstract List<Directions> search(ISearchProblem<T> problem);

    protected List<Directions> generalSearch(ISearchProblem<T> problem, AbstractQueue<SearchNode<T>> frontier,
                                             IHeuristic<T> heuristic) {
        T startState = problem.getStartState();
        SearchNode<T> searchNode = new SearchNode<>(
                startState, null, null, 0,
                heuristic.calculateHeuristic(startState, problem)
        );

        Set<T> expanded = new HashSet<>();
        frontier.add(searchNode);

        while (!frontier.isEmpty()) {
            SearchNode<T> node = frontier.remove();
            if (problem.isGoalState(node.getState())) {
                return node.backtrack();
            }

            if (expanded.contains(node.getState())) {
                continue;
            }

            expanded.add(node.getState());

            for (Successor<T> successor : problem.getSuccessors(node.getState())) {

                SearchNode<T> newNode = new SearchNode<>(successor.state, node, successor.action,
                        node.getCost() + successor.stepCost, heuristic.calculateHeuristic(node.getState(), problem));

                frontier.add(newNode);
            }
        }

        return null;
    }
}
