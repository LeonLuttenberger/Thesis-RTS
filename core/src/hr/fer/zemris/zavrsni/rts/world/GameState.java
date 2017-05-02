package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.resources.Resource;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameState implements IGameState {

    private ILevel level;

    private int minerals;

    public GameState() {
        reset();
    }

    @Override
    public void reset() {
        minerals = 0;

        if (level != null) {
            ArrayList<Unit> units = new ArrayList<>(level.getUnits());
            units.forEach(level::removeUnit);

            ArrayList<Building> buildings = new ArrayList<>(level.getBuildings());
            buildings.forEach(level::removeBuilding);

            ArrayList<Resource> resources = new ArrayList<>(level.getResources());
            resources.forEach(level::removeResource);
        }
    }

    @Override
    public ILevel getLevel() {
        return level;
    }

    @Override
    public void setLevel(ILevel level) {
        this.level = level;
    }

    @Override
    public void selectUnitsInArea(Vector3 areaStart, Vector3 areaEnd) {
        Rectangle selectionArea = new Rectangle(
                Math.min(areaStart.x, areaEnd.x),
                Math.min(areaStart.y, areaEnd.y),
                Math.abs(areaEnd.x - areaStart.x),
                Math.abs(areaEnd.y - areaStart.y)
        );

        for (Unit unit : level.getUnits()) {
            if (selectionArea.contains(unit.getCenterX(), unit.getCenterY())) {
                unit.setSelected(true);
            } else {
                unit.setSelected(false);
            }
        }
    }

    @Override
    public List<Unit> getSelectedUnits() {
        return level.getUnits().stream()
                .filter(Unit::isSelected)
                .collect(Collectors.toList());
    }

    @Override
    public int getMinerals() {
        return minerals;
    }

    @Override
    public void addMinerals(int newMinerals) {
        minerals += newMinerals;
    }
}
