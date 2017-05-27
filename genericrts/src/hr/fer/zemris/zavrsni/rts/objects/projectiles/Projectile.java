package hr.fer.zemris.zavrsni.rts.objects.projectiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.IDamageTrackable;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.objects.IRangedAttacker;

public abstract class Projectile extends AbstractGameObject {

    private static final float HIT_RADIUS = 15f;
    private static final long serialVersionUID = 8260098682213212021L;

    private transient final TextureRegion region;
    protected final ILevel level;
    protected final IRangedAttacker<? extends AbstractGameObject> source;
    protected final IDamageable<? extends AbstractGameObject> target;

    protected final float startX;
    protected final float startY;
    protected final float destinationX;
    protected final float destinationY;

    private int attackPower;

    private boolean isUsedUp;

    public Projectile(TextureRegion region, ILevel level,
                      IRangedAttacker<? extends AbstractGameObject> source,
                      IDamageable<? extends AbstractGameObject> target, int attackPower) {

        this.region = region;
        this.level = level;
        this.source = source;
        this.target = target;
        this.attackPower = attackPower;

        this.startX = source.getAttacker().getCenterX();
        this.startY = source.getAttacker().getCenterY();
        this.destinationX = target.getObject().getCenterX();
        this.destinationY = target.getObject().getCenterY();

        this.setCenterX(startX);
        this.setCenterY(startY);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(region.getTexture(), position.x, position.y, origin.x, origin.y,
                dimension.x, dimension.y, scale.x, scale.y, rotation,
                region.getRegionX(), region.getRegionY(), region.getRegionWidth(),
                region.getRegionHeight(), false, false);
    }

    @Override
    public void update(float deltaTime) {
        if (isUsedUp()) return;

        if (AbstractGameObject.distanceBetween(this, target.getObject()) < HIT_RADIUS) {
            isUsedUp = true;
            if (target instanceof IDamageTrackable) {
                target.removeHitPoints(attackPower);
            }
            return;
        }

        calculateVelocity();
        super.update(deltaTime);
    }

    public boolean isUsedUp() {
        if (getCenterX() < -level.getTileWidth()) return true;
        if (getCenterX() > (level.getTileWidth() + 1) * level.getWidth()) return true;
        if (getCenterY() < -level.getTileHeight()) return true;
        if (getCenterY() > (level.getTileHeight() +1) * level.getHeight()) return true;

        return isUsedUp;
    }

    protected abstract void calculateVelocity();
}
