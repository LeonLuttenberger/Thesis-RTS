package hr.fer.zemris.zavrsni.rts.objects.buildings;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.IUpdatable;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.IDamageTrackable;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public abstract class Building extends AbstractGameObject implements IUpdatable, IDamageTrackable {

    private final TextureRegion region;
    protected final ILevel level;

    protected final int maxHitPoints;
    protected int currentHitPoints;

    private float timeSinceDamageLastTaken;

    public Building(TextureRegion region, ILevel level, float width, float height, int maxHitPoints) {
        this.region = region;
        this.level = level;
        this.dimension.set(width, height);

        this.maxHitPoints = maxHitPoints;
        this.currentHitPoints = maxHitPoints;
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
        timeSinceDamageLastTaken += deltaTime;
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
        timeSinceDamageLastTaken = 0;
    }

    @Override
    public float timeSinceDamageLastTaken() {
        return timeSinceDamageLastTaken;
    }
}
