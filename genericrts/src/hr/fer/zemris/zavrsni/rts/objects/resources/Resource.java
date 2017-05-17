package hr.fer.zemris.zavrsni.rts.objects.resources;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;

public abstract class Resource extends AbstractGameObject implements IDamageable<Resource> {

    private final TextureRegion region;
    private final float terrainModifier;

    private final int maxDurability;
    private int remainingDurability;

    public Resource(TextureRegion region, ILevel level, float terrainModifier, int maxDurability) {
        this.region = region;
        this.dimension.x = level.getTileWidth();
        this.dimension.y = level.getTileHeight();

        this.terrainModifier = terrainModifier;
        this.maxDurability = maxDurability;
        this.remainingDurability = maxDurability;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(region.getTexture(), position.x, position.y, origin.x,
                origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
                region.getRegionX(), region.getRegionY(), region.getRegionWidth(),
                region.getRegionHeight(), false, false);
    }

    public float getTerrainModifier() {
        return terrainModifier;
    }

    @Override
    public int getMaxHitPoints() {
        return maxDurability;
    }

    @Override
    public int getCurrentHitPoints() {
        return remainingDurability;
    }

    @Override
    public void addHitPoints(int repair) {
        throw new UnsupportedOperationException("Cannot \'repair\' a resource.");
    }

    @Override
    public void removeHitPoints(int damage) {
        remainingDurability = Math.max(remainingDurability - damage, 0);
    }
}
