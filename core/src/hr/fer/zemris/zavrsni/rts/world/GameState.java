package hr.fer.zemris.zavrsni.rts.world;

import hr.fer.zemris.zavrsni.rts.objects.units.Squad;
import hr.fer.zemris.zavrsni.rts.objects.units.player.PlayerUnit;

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
        List<PlayerUnit> units = level.getPlayerUnits().stream()
                .filter(PlayerUnit::isSelected)
                .collect(Collectors.toList());

        if (units.isEmpty()) return null;

        Squad newSquad = new Squad(units, level);
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

    @Override
    public void removeMinerals(int mineralsToRemove) {
        minerals -= mineralsToRemove;
    }
}
