package hr.fer.zemris.zavrsni.rts.objects.resources;

import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.common.IGameState;
import hr.fer.zemris.zavrsni.rts.common.ILevel;

public class ResourceBoulder extends Resource {

    private static final int MINERALS = 100;
    private static final int DURABILITY = 1000;

    public ResourceBoulder(ILevel level) {
        super(Assets.getInstance().getResources().rock, level, 0, DURABILITY);
    }

    @Override
    public void onDestroyed(IGameState gameState) {
        gameState.addResource("minerals", MINERALS);
    }
}
