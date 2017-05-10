package hr.fer.zemris.zavrsni.rts.objects.units.hostile;

import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public class AlienBugUnit extends HostileUnit {

    private static final int UNIT_WIDTH = 48;
    private static final int UNIT_HEIGHT = 48;
    private static final float DEFAULT_SPEED = 200;
    private static final float MAX_HEALTH = 20;

    public AlienBugUnit(ILevel level) {
        super(Assets.getInstance().getUnits().bugAnimation, level, UNIT_WIDTH, UNIT_HEIGHT, DEFAULT_SPEED, MAX_HEALTH);
    }
}
