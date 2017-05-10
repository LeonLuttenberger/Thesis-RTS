package hr.fer.zemris.zavrsni.rts.objects.units.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public abstract class PlayerUnit extends Unit {

    private boolean isSelected = false;

    public PlayerUnit(Animation<TextureRegion> animation, ILevel level, float width, float height,
                      float defaultSpeed, int maxHealth, float attackRange, int attackPower) {
        super(animation, level, width, height, defaultSpeed, maxHealth, attackRange, attackPower);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public boolean isHostile() {
        return false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
