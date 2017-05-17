package hr.fer.zemris.zavrsni.rts.common;

import hr.fer.zemris.zavrsni.rts.objects.units.Squad;

import java.util.List;

public interface IGameState {

    ILevel getLevel();

    void setLevel(ILevel level);

    Squad createSquadFromSelected();

    List<Squad> getSquads();

    void removeSquad(Squad squad);

    void reset();

    int getResources(String name);

    void addResource(String name, int dResource);

    void removeResource(String name, int dResource);
}
