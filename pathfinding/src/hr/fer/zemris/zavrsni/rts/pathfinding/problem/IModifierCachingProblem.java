package hr.fer.zemris.zavrsni.rts.pathfinding.problem;

public interface IModifierCachingProblem<T> extends ISearchProblem<T> {

    float getCachedModifier(T position);

}