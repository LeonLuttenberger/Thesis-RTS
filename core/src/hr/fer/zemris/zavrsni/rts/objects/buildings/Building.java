package hr.fer.zemris.zavrsni.rts.objects.buildings;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.IUpdatable;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;

public abstract class Building extends AbstractGameObject implements IUpdatable, IDamageable {

    private final TextureRegion region;

    protected final int maxHitPoints;
    protected int currentHitPoints;

    public Building(TextureRegion region, float width, float height, int maxHitPoints) {
        this.region = region;
        this.dimension.set(width, height);

        this.maxHitPoints = maxHitPoints;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(region.getTexture(), position.x, position.y, origin.x, origin.y,
                dimension.x, dimension.y, scale.x, scale.y, rotation,
                region.getRegionX(), region.getRegionY(), region.getRegionWidth(),
                region.getRegionHeight(), false, false);
    }

    @Override
    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    @Override
    public int getCurrentHitPoints() {
        return currentHitPoints;
    }

    @Override
    public void addHitPoints(int repair) {
        currentHitPoints = Math.min(currentHitPoints + repair, maxHitPoints);
    }

    @Override
    public void removeHitPoints(int damage) {
        currentHitPoints = Math.max(currentHitPoints - damage, 0);
    }
}
