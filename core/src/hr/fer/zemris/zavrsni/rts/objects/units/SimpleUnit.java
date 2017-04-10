package hr.fer.zemris.zavrsni.rts.objects.units;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.objects.AbstractMovableObject;

public class SimpleUnit extends AbstractMovableObject {

    private boolean isSelected;

    public SimpleUnit() {
        super(Assets.getInstance().getUnits().simpleUnit);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (isSelected) {
            Color old = batch.getColor();
            batch.setColor(Color.RED);

            super.render(batch);

            batch.setColor(old);
        } else {
            super.render(batch);
        }
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
