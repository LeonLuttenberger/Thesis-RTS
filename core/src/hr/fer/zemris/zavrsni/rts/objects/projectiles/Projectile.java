package hr.fer.zemris.zavrsni.rts.objects.projectiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.AbstractMovableObject;
import hr.fer.zemris.zavrsni.rts.objects.IDamageTrackable;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public abstract class Projectile extends AbstractMovableObject {

    private static final float HIT_RADIUS = 15f;

    private final TextureRegion region;
    protected final ILevel level;
    protected final AbstractGameObject source;
    protected final AbstractGameObject target;

    protected final float startX;
    protected final float startY;
    protected final float destinationX;
    protected final float destinationY;

    private int attackPower;

    private boolean isUsedUp;

    public Projectile(TextureRegion region, ILevel level,
                      AbstractGameObject source, AbstractGameObject target, int attackPower) {

        this.region = region;
        this.level = level;
        this.source = source;
        this.target = target;
        this.attackPower = attackPower;

        this.startX = source.getCenterX();
        this.startY = source.getCenterY();
        this.destinationX = target.getCenterX();
        this.destinationY = target.getCenterY();

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

        if (AbstractGameObject.distanceBetween(this, target) < HIT_RADIUS) {
            isUsedUp = true;
            if (target instanceof IDamageTrackable) {
                ((IDamageTrackable) target).removeHitPoints(attackPower);
            }
            return;
        }

        calculateVelocity();
        super.update(deltaTime);
    }

    public boolean isUsedUp() {
        if (getCenterX() < 0) return true;
        if (getCenterX() > level.getTileWidth() * level.getWidth()) return true;
        if (getCenterY() < 0) return true;
        if (getCenterY() > level.getTileHeight() * level.getHeight()) return true;

        return isUsedUp;
    }

    protected abstract void calculateVelocity();
}
