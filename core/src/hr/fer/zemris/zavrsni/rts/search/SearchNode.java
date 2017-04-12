package hr.fer.zemris.zavrsni.rts.search;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SearchNode<T> {

    public static final Comparator<SearchNode<?>> BY_COST = Comparator.comparingDouble(s -> s.cost);
    public static final Comparator<SearchNode<?>> BY_HEURISTIC = Comparator.comparingDouble(s -> s.heuristic);
    public static final Comparator<SearchNode<?>> BY_COST_AND_HEURISTIC = Comparator.comparingDouble(s -> s.cost + s.heuristic);

    private T state;
    private SearchNode<T> parent;
    private Transition transition;
    private double cost;
    private double heuristic;

    public SearchNode(T state, SearchNode<T> parent, Transition transition, double cost, double heuristic) {
        this.state = Objects.requireNonNull(state);
        this.parent = parent;
        this.transition = transition;
        this.cost = cost;
        this.heuristic = heuristic;
    }

    public T getState() {
        return state;
    }

    public SearchNode<T> getParent() {
        return parent;
    }

    public Transition getTransition() {
        return transition;
    }

    public double getCost() {
        return cost;
    }

    public double getHeuristic() {
        return heuristic;
    }

    public boolean isRootNode() {
        return parent == null;
    }

    public List<Transition> backtrack() {
        LinkedList<Transition> moves = new LinkedList<>();

        SearchNode<T> node = this;
        while (!node.isRootNode()) {
            moves.addFirst(node.transition);
            node = node.getParent();
        }

        return moves;
    }
}
