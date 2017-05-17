package hr.fer.zemris.zavrsni.rts.objects.resources;

import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.common.GameResources;
import hr.fer.zemris.zavrsni.rts.common.IGameState;
import hr.fer.zemris.zavrsni.rts.common.ILevel;

public class ResourceBoulder extends Resource {

    private static final int AMOUNT_OF_MINERALS = 100;
    private static final int DURABILITY = 1000;

    public ResourceBoulder(ILevel level) {
        super(Assets.getInstance().getResources().rock, level, 0, DURABILITY);
    }

    @Override
    public void onDestroyed(IGameState gameState) {
        gameState.addResource(GameResources.KEY_MINERALS, AMOUNT_OF_MINERALS);
    }
}
