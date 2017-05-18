package hr.fer.zemris.zavrsni.rts.common;

import hr.fer.zemris.zavrsni.rts.objects.units.Squad;

import java.util.List;
import java.util.Set;

public interface IGameState {

    ILevel getLevel();

    void setLevel(ILevel level);

    Squad createSquadFromSelected();

    Set<String> savedSelectionKeys();

    void saveCurrentSelection(String key);

    void selectSavedSelection(String key);

    List<Squad> getSquads();

    void removeSquad(Squad squad);

    void reset();

    int getResources(String name);

    void addResource(String name, int dResource);

    void removeResource(String name, int dResource);
}
