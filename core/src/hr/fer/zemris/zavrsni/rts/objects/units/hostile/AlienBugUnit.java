package hr.fer.zemris.zavrsni.rts.objects.units.hostile;

import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public class AlienBugUnit extends HostileUnit {

    private static final int UNIT_WIDTH = 32;
    private static final int UNIT_HEIGHT = 32;
    private static final float DEFAULT_SPEED = 110;
    private static final int MAX_HEALTH = 200;
    private static final int ATTACK_RANGE = 30;
    private static final int ATTACK_POWER = 20;
    private static final float ATTACK_COOLDOWN = 0.8f;

    public AlienBugUnit(ILevel level) {
        super(Assets.getInstance().getUnits().bugAnimation, level, UNIT_WIDTH, UNIT_HEIGHT,
                DEFAULT_SPEED, MAX_HEALTH, ATTACK_RANGE, ATTACK_POWER, ATTACK_COOLDOWN);
    }

    @Override
    public boolean isSupport() {
        return false;
    }
}
