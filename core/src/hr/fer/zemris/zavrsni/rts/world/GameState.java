package hr.fer.zemris.zavrsni.rts.world;

import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.resources.Resource;
import hr.fer.zemris.zavrsni.rts.objects.units.Squad;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameState implements IGameState {

    private ILevel level;

    private int minerals;

    private final List<Squad> squads = new ArrayList<>();

    public GameState() {
        reset();
    }

    @Override
    public void reset() {
        minerals = 0;
        squads.clear();

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
    public Squad createSquadFromSelected() {
        List<Unit> units = level.getUnits().stream()
                .filter(Unit::isSelected)
                .collect(Collectors.toList());

        Squad newSquad = new Squad(units);
        squads.add(newSquad);

        return newSquad;
    }

    @Override
    public List<Squad> getSquads() {
        return squads;
    }

    @Override
    public void removeSquad(Squad squad) {
        squads.remove(squad);
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
