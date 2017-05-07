package hr.fer.zemris.zavrsni.rts.search;

import java.util.Queue;
import java.util.Set;

public class SearchResult<T> {

    private final Queue<SearchNode<T>> statesQueue;
    private final Set<SearchNode<T>> closedSet;
    private final Queue<SearchNode<T>> frontierQueue;

    public SearchResult(Queue<SearchNode<T>> statesQueue, Set<SearchNode<T>> closedSet, Queue<SearchNode<T>> frontierQueue) {
        this.statesQueue = statesQueue;
        this.closedSet = closedSet;
        this.frontierQueue = frontierQueue;
    }

    public Queue<SearchNode<T>> getStatesQueue() {
        return statesQueue;
    }

    public Set<SearchNode<T>> getClosedSet() {
        return closedSet;
    }

    public Queue<SearchNode<T>> getFrontierQueue() {
        return frontierQueue;
    }
}
