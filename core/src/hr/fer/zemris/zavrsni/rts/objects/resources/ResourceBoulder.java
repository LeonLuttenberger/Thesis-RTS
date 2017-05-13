package hr.fer.zemris.zavrsni.rts.objects.resources;

import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.world.IGameState;

public class ResourceBoulder extends Resource {

    private static final int MINERALS = 100;
    private static final int DURABILITY = 1000;

    public ResourceBoulder() {
        super(Assets.getInstance().getResources().rock, 0, DURABILITY);
    }

    @Override
    public void onResourceDestroyed(IGameState gameState) {
        gameState.addMinerals(MINERALS);
    }
}
