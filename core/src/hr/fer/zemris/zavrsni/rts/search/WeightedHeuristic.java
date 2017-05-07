package hr.fer.zemris.zavrsni.rts.search;

public class WeightedHeuristic<T, H extends IHeuristic<T>> implements IHeuristic<T> {

    private final H heuristic;
    private final double weight;

    public WeightedHeuristic(H heuristic, double weight) {
        this.heuristic = heuristic;
        this.weight = weight;
    }

    @Override
    public double calculateHeuristic(T state, ISearchProblem<T> problem) {
        return heuristic.calculateHeuristic(state, problem) * weight;
    }

    public H getHeuristic() {
        return heuristic;
    }
}
