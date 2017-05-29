package hr.fer.zemris.zavrsni.rts.objects.units.hostile;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.units.HostileUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.IBuildableUnit;

public class StickmanUnit extends HostileUnit implements IBuildableUnit {

    private static final long serialVersionUID = -136607749025608420L;

    private static final float ENEMY_DETECTION_RANGE = 150f;
    private static final int UNIT_WIDTH = 32;
    private static final int UNIT_HEIGHT = 32;
    private static final float DEFAULT_SPEED = 200;
    private static final int MAX_HEALTH = 100;
    private static final int ATTACK_RANGE = 50;
    private static final int ATTACK_POWER = 5;
    private static final float ATTACK_COOLDOWN = 0.3f;

    public StickmanUnit(ILevel level) {
        super(level, UNIT_WIDTH, UNIT_HEIGHT, DEFAULT_SPEED, MAX_HEALTH,
                ATTACK_RANGE, ATTACK_POWER, ATTACK_COOLDOWN, ENEMY_DETECTION_RANGE);
    }

    @Override
    public Animation<TextureRegion> loadAnimation() {
        return Assets.getInstance().getUnits().stickmanAnimation;
    }

    @Override
    public boolean isSupport() {
        return false;
    }

    @Override
    public int getTrainingCost() {
        return 500;
    }
}
