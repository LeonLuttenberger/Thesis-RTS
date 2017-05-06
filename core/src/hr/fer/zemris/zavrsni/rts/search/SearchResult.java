package hr.fer.zemris.zavrsni.rts.search;

import java.util.Queue;
import java.util.Set;

public class SearchResult<T> {

    private final Queue<T> statesQueue;
    private final Set<T> closedSet;
    private final Queue<SearchNode<T>> frontierQueue;

    public SearchResult(Queue<T> statesQueue, Set<T> closedSet, Queue<SearchNode<T>> frontierQueue) {
        this.statesQueue = statesQueue;
        this.closedSet = closedSet;
        this.frontierQueue = frontierQueue;
    }

    public Queue<T> getStatesQueue() {
        return statesQueue;
    }

    public Set<T> getClosedSet() {
        return closedSet;
    }

    public Queue<SearchNode<T>> getFrontierQueue() {
        return frontierQueue;
    }
}
