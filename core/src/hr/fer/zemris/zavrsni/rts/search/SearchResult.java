package hr.fer.zemris.zavrsni.rts.search;

import java.util.Map;
import java.util.Queue;

public class SearchResult<T> {

    private final Queue<SearchNode<T>> statesQueue;
    private final Map<T, Double> closedMap;
    private final Queue<SearchNode<T>> frontierQueue;

    public SearchResult(Queue<SearchNode<T>> statesQueue, Map<T, Double> closedMap, Queue<SearchNode<T>> frontierQueue) {
        this.statesQueue = statesQueue;
        this.closedMap = closedMap;
        this.frontierQueue = frontierQueue;
    }

    public Queue<SearchNode<T>> getStatesQueue() {
        return statesQueue;
    }

    public Map<T, Double> getClosedSet() {
        return closedMap;
    }

    public Queue<SearchNode<T>> getFrontierQueue() {
        return frontierQueue;
    }
}
