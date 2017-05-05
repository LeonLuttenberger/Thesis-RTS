package hr.fer.zemris.zavrsni.rts.search;

import java.util.List;
import java.util.Queue;
import java.util.Set;

public class SearchResult<T> {

    private final List<Transition> transitions;
    private final Set<T> closedSet;
    private final Queue<SearchNode<T>> queue;

    public SearchResult(List<Transition> transitions, Set<T> closedSet, Queue<SearchNode<T>> queue) {
        this.transitions = transitions;
        this.closedSet = closedSet;
        this.queue = queue;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public Set<T> getClosedSet() {
        return closedSet;
    }

    public Queue<SearchNode<T>> getQueue() {
        return queue;
    }
}
