package hr.fer.zemris.zavrsni.rts.world;

import hr.fer.zemris.zavrsni.rts.objects.units.Squad;

import java.util.List;

public interface IGameState {

    ILevel getLevel();

    void setLevel(ILevel level);

    Squad createSquadFromSelected();

    List<Squad> getSquads();

    void removeSquad(Squad squad);

    void reset();

    int getMinerals();

    void addMinerals(int minerals);

    void removeMinerals(int minerals);
}
