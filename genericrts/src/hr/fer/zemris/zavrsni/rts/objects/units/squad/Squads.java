package hr.fer.zemris.zavrsni.rts.objects.units.squad;

import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;

import java.util.ArrayList;
import java.util.Set;

public final class Squads {

    private Squads() {}

    public static ISquad from(Set<Unit> units, ILevel level) {
        float joinDistance = Math.min(level.getTileWidth(), level.getTileHeight()) * 4;

        ISquad squad = new CompositeSquad(new ArrayList<>(), level, joinDistance);
        units.forEach(squad::addUnit);

        return squad;
    }
}
