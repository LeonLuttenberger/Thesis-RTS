package hr.fer.zemris.zavrsni.rts.objects.units.squad;

import hr.fer.zemris.zavrsni.rts.common.IUpdateable;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;

import java.io.Serializable;
import java.util.Set;

public interface ISquad extends IUpdateable, Serializable {

    Set<Unit> getSquadMembers();

    void addUnit(Unit unit);

    void removeUnit(Unit unit);

    void sendToLocation(float x, float y);

    boolean isSearchStopped();

    void stopSearch();
}
