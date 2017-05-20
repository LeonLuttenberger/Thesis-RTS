package hr.fer.zemris.zavrsni.rts.objects.buildings;

import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.common.ILevel;

public class AlienBaseBuilding extends HostileBuilding {

    private static final int WIDTH = 96;
    private static final int HEIGHT = 64;
    private static final int MAX_HIT_POINTS = 300;

    public AlienBaseBuilding(ILevel level) {
        super(Assets.getInstance().getBuildings().alienBase, level, WIDTH, HEIGHT, MAX_HIT_POINTS);
    }
}
