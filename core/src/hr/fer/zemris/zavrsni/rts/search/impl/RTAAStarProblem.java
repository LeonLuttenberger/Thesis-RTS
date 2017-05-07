package hr.fer.zemris.zavrsni.rts.search.impl;

import hr.fer.zemris.zavrsni.rts.search.CachingProblem;
import hr.fer.zemris.zavrsni.rts.search.ISearchProblem;
import hr.fer.zemris.zavrsni.rts.search.LimitedExpansionProblem;

public class RTAAStarProblem<T, S extends ISearchProblem<T>> extends CachingProblem<T, LimitedExpansionProblem<T, S>> {

    private T newStartState = null;

    public RTAAStarProblem(S problem, int lookahead) {
        super(new LimitedExpansionProblem<>(problem, lookahead));
    }

    @Override
    public T getStartState() {
        if (newStartState == null) {
            return super.getStartState();
        } else {
            return newStartState;
        }
    }

    public void setNewStartState(T newStartState) {
        this.newStartState = newStartState;
        this.getProblem().reset();
    }
}
