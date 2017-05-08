package hr.fer.zemris.zavrsni.rts.objects.resources;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.util.Constants;
import hr.fer.zemris.zavrsni.rts.world.IGameState;

public abstract class Resource extends AbstractGameObject {

    private final TextureRegion region;
    private final float terrainModifier;
    private final float totalDurability;

    private float remainingDurability;

    public Resource(TextureRegion region, float terrainModifier, float totalDurability) {
        this.region = region;
        this.dimension.x = Constants.TILE_WIDTH;
        this.dimension.y = Constants.TILE_HEIGHT;

        this.terrainModifier = terrainModifier;
        this.totalDurability = totalDurability;
        this.remainingDurability = totalDurability;
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

    public float getTotalDurability() {
        return totalDurability;
    }

    public float getRemainingDurability() {
        return remainingDurability;
    }

    public void removeDurability(float delta) {
        remainingDurability -= delta;
    }

    public abstract void onResourceDestroyed(IGameState gameState);
}
