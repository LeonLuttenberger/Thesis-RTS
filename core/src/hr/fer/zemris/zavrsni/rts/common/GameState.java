package hr.fer.zemris.zavrsni.rts.common;

import hr.fer.zemris.zavrsni.rts.objects.units.PlayerUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.Squad;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameState implements IGameState {

    private ILevel level;

    private Map<String, Integer> resources = new HashMap<>();

    private final List<Squad> squads = new ArrayList<>();
    private final Map<Unit, Squad> unitSquadMap = new HashMap<>();

    public GameState() {
        reset();
    }

    @Override
    public void reset() {
        resources.put(GameResources.KEY_MINERALS, 0);
        squads.clear();

        if (level != null) {
            level.reset();
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
        List<Unit> units = level.getPlayerUnits().stream()
                .filter(PlayerUnit::isSelected)
                .collect(Collectors.toList());

        if (units.isEmpty()) return null;

        for (Unit unit : units) {
            Squad previousSquad = unitSquadMap.get(unit);

            if (previousSquad != null) {
                previousSquad.removeUnit(unit);
            }
        }

        Squad newSquad = new Squad(units, level);
        squads.add(newSquad);

        for (Unit unit : units) {
            unitSquadMap.put(unit, newSquad);
        }

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
    public int getResources(String name) {
        if (!resources.containsKey(name)) {
            throw new NoSuchResourceException();
        }

        return resources.get(name);
    }

    @Override
    public void addResource(String name, int dResource) {
        if (!resources.containsKey(name)) {
            throw new NoSuchResourceException();
        }

        resources.put(name, resources.get(name) + dResource);
    }

    @Override
    public void removeResource(String name, int dResource) {
        if (!resources.containsKey(name)) {
            throw new NoSuchResourceException();
        }

        resources.put(name, resources.get(name) - dResource);
    }
}
