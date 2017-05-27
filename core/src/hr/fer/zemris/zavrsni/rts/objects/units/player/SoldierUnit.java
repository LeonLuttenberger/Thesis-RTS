package hr.fer.zemris.zavrsni.rts.objects.units.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.objects.IRangedAttacker;
import hr.fer.zemris.zavrsni.rts.objects.projectiles.PlayerBullet;
import hr.fer.zemris.zavrsni.rts.objects.projectiles.Projectile;
import hr.fer.zemris.zavrsni.rts.objects.units.IBuildableUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.PlayerUnit;

public class SoldierUnit extends PlayerUnit implements IBuildableUnit, IRangedAttacker<SoldierUnit> {

    private static final long serialVersionUID = -4096073723947855323L;

    private static final int UNIT_WIDTH = 32;
    private static final int UNIT_HEIGHT = 32;
    private static final float DEFAULT_SPEED = 100;
    private static final int MAX_HEALTH = 300;
    private static final int ATTACK_RANGE = 150;
    private static final int ATTACK_POWER = 20;
    private static final float ATTACK_COOLDOWN = 1;
    private static final int TRAINING_COST = 4000;

    public SoldierUnit(ILevel level) {
        super(level, UNIT_WIDTH, UNIT_HEIGHT,
                DEFAULT_SPEED, MAX_HEALTH, ATTACK_RANGE, ATTACK_POWER, ATTACK_COOLDOWN);
    }

    @Override
    public Animation<TextureRegion> loadAnimation() {
        return Assets.getInstance().getUnits().soldierAnimation;
    }

    @Override
    public boolean isSupport() {
        return false;
    }

    @Override
    public int getTrainingCost() {
        return TRAINING_COST;
    }

    @Override
    public Projectile rangedAttack(IDamageable<? extends AbstractGameObject> target) {
        return new PlayerBullet(level, this, target, ATTACK_POWER);
    }
}
