package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.math.Vector3;

public class GameState {

    private Level level;

    public GameState() {
        reset();
    }

    private void reset() {

    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void selectUnitsInArea(Vector3 areaStart, Vector3 areaEnd) {
        //TODO
        level.getUnit().setSelected(true);
    }
}
