package hr.fer.zemris.zavrsni.rts.common;

import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.objects.units.PlayerUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.Squad;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class GameState implements IGameState {

    private ILevel level;

    private Map<String, Integer> resources = new HashMap<>();
    {
        resources.put(GameResources.KEY_MINERALS, 0);
    }

    private final List<Squad> squads = new ArrayList<>();

    private final Map<Unit, Squad> unitSquadMap = new HashMap<>();
    private final Map<String, Set<Unit>> savedSelections = new HashMap<>();

    public GameState(ILevel level) {
        this.level = Objects.requireNonNull(level);
    }

    @Override
    public void reset() {
        resources.put(GameResources.KEY_MINERALS, 0);
        squads.clear();
        unitSquadMap.clear();
        savedSelections.clear();

        level.reset();
    }

    @Override
    public ILevel getLevel() {
        return level;
    }

    @Override
    public void setLevel(ILevel level) {
        this.level = Objects.requireNonNull(level);
    }

    @Override
    public Squad createSquadFromSelected() {
        Set<Unit> units = level.getPlayerUnits().stream()
                .filter(PlayerUnit::isSelected)
                .collect(Collectors.toSet());

        if (units.isEmpty()) return null;

        for (Unit unit : units) {
            Squad previousSquad = unitSquadMap.get(unit);

            if (previousSquad != null) {
                previousSquad.removeUnit(unit);
            }
        }

        Squad newSquad = new Squad(units, level);

        addSquad(newSquad);
        return newSquad;
    }

    @Override
    public List<Squad> getSquads() {
        return squads;
    }

    @Override
    public Set<String> savedSelectionKeys() {
        return null;
    }

    @Override
    public void saveCurrentSelection(String key) {
        Set<Unit> units = level.getPlayerUnits().stream()
                .filter(PlayerUnit::isSelected)
                .collect(Collectors.toSet());

        savedSelections.put(key, units);
    }

    private void clearSelection() {
        for (PlayerUnit unit : level.getPlayerUnits()) {
            unit.setSelected(false);
        }
    }

    @Override
    public void selectSavedSelection(String key) {
        clearSelection();

        Set<Unit> units = savedSelections.get(key);
        if (units == null) return;

        units.removeIf(IDamageable::isDestroyed);
        if (units.isEmpty()) {
            savedSelections.remove(key);
            return;
        }

        for (PlayerUnit unit : level.getPlayerUnits()) {
            unit.setSelected(units.contains(unit));
        }
    }

    @Override
    public void addSquad(Squad squad) {
        squads.add(squad);

        for (Unit unit : squad.getSquadMembers()) {
            unitSquadMap.put(unit, squad);
        }
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
