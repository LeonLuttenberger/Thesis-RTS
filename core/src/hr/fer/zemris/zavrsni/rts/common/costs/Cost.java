package hr.fer.zemris.zavrsni.rts.common.costs;

import hr.fer.zemris.zavrsni.rts.common.IGameState;

import static hr.fer.zemris.zavrsni.rts.common.GameResources.KEY_MINERALS;

public class Cost {

    public final int minerals;

    public Cost(int minerals) {
        this.minerals = minerals;
    }

    public boolean isSatisfied(IGameState gameState) {
        return gameState.getResources(KEY_MINERALS) >= minerals;
    }

    public void apply(IGameState gameState) {
        gameState.removeResource(KEY_MINERALS, minerals);
    }
}
