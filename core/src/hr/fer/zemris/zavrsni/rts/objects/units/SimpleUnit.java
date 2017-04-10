package hr.fer.zemris.zavrsni.rts.objects.units;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.objects.AbstractMovableObject;

public class SimpleUnit extends AbstractMovableObject {

    private static final String TAG = SimpleUnit.class.getName();

    private TextureRegion regionUnit;

    public SimpleUnit() {
        regionUnit = Assets.getInstance().getUnits().simpleUnit;
        dimension.x = regionUnit.getRegionWidth();
        dimension.y = regionUnit.getRegionHeight();
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(regionUnit.getTexture(), position.x, position.y, origin.x,
                origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
                regionUnit.getRegionX(), regionUnit.getRegionY(), regionUnit.getRegionWidth(),
                regionUnit.getRegionHeight(), false, false);
    }
}
