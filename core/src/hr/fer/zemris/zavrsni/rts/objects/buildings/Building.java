package hr.fer.zemris.zavrsni.rts.objects.buildings;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.IUpdatable;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;

public abstract class Building extends AbstractGameObject implements IUpdatable {

    private final TextureRegion region;

    public Building(TextureRegion region, float width, float height) {
        this.region = region;
        this.dimension.set(width, height);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(region.getTexture(), position.x, position.y, origin.x, origin.y,
                dimension.x, dimension.y, scale.x, scale.y, rotation,
                region.getRegionX(), region.getRegionY(), region.getRegionWidth(),
                region.getRegionHeight(), false, false);
    }
}
