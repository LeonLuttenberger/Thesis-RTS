package hr.fer.zemris.zavrsni.rts.pathfinding.algorithms;

import hr.fer.zemris.zavrsni.rts.pathfinding.SearchNode;
import hr.fer.zemris.zavrsni.rts.pathfinding.SearchResult;
import hr.fer.zemris.zavrsni.rts.pathfinding.heuristic.IHeuristic;
import hr.fer.zemris.zavrsni.rts.pathfinding.problem.ISearchProblem;
import hr.fer.zemris.zavrsni.rts.pathfinding.problem.ISearchProblem.Successor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class AStarSearch<T> implements ISearchAlgorithm<T> {

    @Override
    public SearchResult<T> search(ISearchProblem<T> problem, IHeuristic<T> heuristic) {
        PriorityQueue<SearchNode<T>> frontier = new PriorityQueue<>(SearchNode.BY_COST_AND_HEURISTIC);

        T startState = problem.getStartState();
        SearchNode<T> startNode = new SearchNode<>(
                startState, null, 0,
                heuristic.calculateHeuristic(startState, problem)
        );

        Map<T, Double> expanded = new HashMap<>();
        frontier.add(startNode);

        while (!frontier.isEmpty()) {
            SearchNode<T> node = frontier.peek();

            if (expanded.containsKey(node.getState())) {
                double previousCost = expanded.get(node.getState());
                if (node.getCost() >= previousCost) {
                    frontier.poll();
                    continue;
                }
            }

            if (problem.isGoalState(node.getState())) {
                return new SearchResult<>(node.backtrack(), expanded, frontier);
            }
            frontier.poll();

            expanded.put(node.getState(), node.getCost());

            List<Successor<T>> successors = problem.getSuccessors(node.getState());
            for (Successor<T> successor : successors) {

                SearchNode<T> newNode = new SearchNode<>(successor.state, node, node.getCost() + successor.stepCost,
                        heuristic.calculateHeuristic(successor.state, problem));

                frontier.add(newNode);
            }
        }

        return null;
    }
}
