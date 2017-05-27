package hr.fer.zemris.zavrsni.rts.objects.projectiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.objects.IRangedAttacker;

public abstract class AbstractBullet extends Projectile {

    private static final long serialVersionUID = 7793220823049865454L;

    private static final int WIDTH = 5;
    private static final int HEIGHT = 5;
    private static final float MAX_SPEED = 800f;

    public AbstractBullet(TextureRegion region, ILevel level,
                          IRangedAttacker<? extends AbstractGameObject> source,
                          IDamageable<? extends AbstractGameObject> target, int attackPower) {

        super(region, level, source, target, attackPower);

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
