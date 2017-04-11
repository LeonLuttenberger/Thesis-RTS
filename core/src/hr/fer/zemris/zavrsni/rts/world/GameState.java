package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import hr.fer.zemris.zavrsni.rts.objects.units.SimpleUnit;

import java.util.List;
import java.util.stream.Collectors;

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
        Rectangle selectionArea = new Rectangle(
                Math.min(areaStart.x, areaEnd.x),
                Math.min(areaStart.y, areaEnd.y),
                Math.abs(areaEnd.x - areaStart.x),
                Math.abs(areaEnd.y - areaStart.y)
        );

        for (SimpleUnit unit : level.getUnits()) {
            if (selectionArea.contains(unit.getPosition().x, unit.getPosition().y)) {
                unit.setSelected(true);
            } else {
                unit.setSelected(false);
            }
        }
    }

    public List<SimpleUnit> getSelectedUnits() {
        return level.getUnits().stream()
                .filter(SimpleUnit::isSelected)
                .collect(Collectors.toList());
    }
}
