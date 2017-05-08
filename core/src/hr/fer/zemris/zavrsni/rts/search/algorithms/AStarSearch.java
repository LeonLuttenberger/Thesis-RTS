package hr.fer.zemris.zavrsni.rts.search.algorithms;

import hr.fer.zemris.zavrsni.rts.search.SearchNode;
import hr.fer.zemris.zavrsni.rts.search.SearchResult;
import hr.fer.zemris.zavrsni.rts.search.heuristic.IHeuristic;
import hr.fer.zemris.zavrsni.rts.search.problem.ISearchProblem;
import hr.fer.zemris.zavrsni.rts.search.problem.ISearchProblem.Successor;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class AStarSearch<T> implements ISearchAlgorithm<T> {

    @Override
    public SearchResult<T> search(ISearchProblem<T> problem, IHeuristic<T> heuristic) {
        PriorityQueue<SearchNode<T>> frontier = new PriorityQueue<>(SearchNode.BY_COST_AND_HEURISTIC);

        T startState = problem.getStartState();
        SearchNode<T> searchNode = new SearchNode<>(
                startState, null, 0,
                heuristic.calculateHeuristic(startState, problem)
        );

        Map<T, Double> expanded = new HashMap<>();
        frontier.add(searchNode);

        while (!frontier.isEmpty()) {
            SearchNode<T> node = frontier.peek();
            if (problem.isGoalState(node.getState())) {
                return new SearchResult<>(node.backtrack(), expanded, frontier);
            }
            frontier.poll();

            if (expanded.containsKey(node.getState())) {
                double previousCost = expanded.get(node.getState());
                if (node.getCost() >= previousCost) {
                    continue;
                }
            }

            expanded.put(node.getState(), node.getCost());

            for (Successor<T> successor : problem.getSuccessors(node.getState())) {

                SearchNode<T> newNode = new SearchNode<>(successor.state, node, node.getCost() + successor.stepCost,
                        heuristic.calculateHeuristic(successor.state, problem));

                frontier.add(newNode);
            }
        }

        return null;
    }
}
