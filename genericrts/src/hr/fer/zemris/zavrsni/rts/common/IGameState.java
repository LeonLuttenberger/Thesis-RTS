package hr.fer.zemris.zavrsni.rts.common;

import hr.fer.zemris.zavrsni.rts.objects.units.squad.ISquad;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface IGameState extends Serializable {

    ILevel getLevel();

    void setLevel(ILevel level);

    ISquad createSquadFromSelected();

    Set<String> savedSelectionKeys();

    void saveCurrentSelection(String key);

    void selectSavedSelection(String key);

    List<ISquad> getSquads();

    void addSquad(ISquad squad);

    void removeSquad(ISquad squad);

    void reset();

    int getResources(String name);

    void addResource(String name, int dResource);

    void removeResource(String name, int dResource);
}
