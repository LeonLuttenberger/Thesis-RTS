package hr.fer.zemris.zavrsni.rts.objects.projectiles;

import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.objects.IRangedAttacker;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public class Bullet extends Projectile {

    private static final int WIDTH = 5;
    private static final int HEIGHT = 5;
    private static final float MAX_SPEED = 200f;

    public Bullet(ILevel level, IRangedAttacker<? extends AbstractGameObject> source,
                  IDamageable<? extends AbstractGameObject> target, int attackPower) {

        super(Assets.getInstance().getOthers().bullet, level, source, target, attackPower);

        dimension.set(WIDTH, HEIGHT);
    }

    @Override
    protected void calculateVelocity() {
        velocity.set(
                destinationX - startX,
                destinationY - startY
        );
        velocity.setLength(MAX_SPEED);
    }
}
