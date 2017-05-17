package hr.fer.zemris.zavrsni.rts.objects.resources;

import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.common.IGameState;
import hr.fer.zemris.zavrsni.rts.util.Constants;

public class ResourceBoulder extends Resource {

    private static final int MINERALS = 100;
    private static final int DURABILITY = 1000;

    public ResourceBoulder() {
        super(Assets.getInstance().getResources().rock, Constants.TILE_WIDTH, Constants.TILE_HEIGHT, 0, DURABILITY);
    }

    @Override
    public void onDestroyed(IGameState gameState) {
        gameState.addResource("minerals", MINERALS);
    }
}
