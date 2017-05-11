package hr.fer.zemris.zavrsni.rts.objects.buildings;

import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public class GeneratorBuilding extends Building {

    private static final int WIDTH = 160;
    private static final int HEIGHT = 256;
    private static final int MAX_HIT_POINTS = 100;

    public GeneratorBuilding(ILevel level) {
        super(Assets.getInstance().getBuildings().generator, level, WIDTH, HEIGHT, MAX_HIT_POINTS);
    }
}
