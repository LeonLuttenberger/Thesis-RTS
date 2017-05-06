package hr.fer.zemris.zavrsni.rts.search;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class SearchNode<T> {

    public static final Comparator<SearchNode<?>> BY_COST = Comparator.comparingDouble(s -> s.cost);
    public static final Comparator<SearchNode<?>> BY_HEURISTIC = Comparator.comparingDouble(s -> s.heuristic);
    public static final Comparator<SearchNode<?>> BY_COST_AND_HEURISTIC = Comparator.comparingDouble(s -> s.cost + s.heuristic);

    private T state;
    private SearchNode<T> parent;
    private double cost;
    private double heuristic;

    public SearchNode(T state, SearchNode<T> parent, double cost, double heuristic) {
        this.state = Objects.requireNonNull(state);
        this.parent = parent;
        this.cost = cost;
        this.heuristic = heuristic;
    }

    public T getState() {
        return state;
    }

    public SearchNode<T> getParent() {
        return parent;
    }


    public double getCost() {
        return cost;
    }

    public double getHeuristic() {
        return heuristic;
    }

    public Queue<T> backtrack() {
        LinkedList<T> moves = new LinkedList<>();

        SearchNode<T> node = this;
        while (node != null) {
            moves.addFirst(node.state);
            node = node.getParent();
        }

        return moves;
    }
}
