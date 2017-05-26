package hr.fer.zemris.zavrsni.rts.common.costs;

import hr.fer.zemris.zavrsni.rts.objects.units.PlayerUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.player.SoldierUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.player.WorkerUnit;

import java.util.HashMap;
import java.util.Map;

public class UnitCosts {

    private UnitCosts() {}

    private static final Map<Class<? extends PlayerUnit>, Cost> unitCosts = new HashMap<>();

    static {
        unitCosts.put(SoldierUnit.class, new Cost(50));
        unitCosts.put(WorkerUnit.class, new Cost(80));
    }

    public static Cost getCostFor(Class<? extends PlayerUnit> buildingType) {
        return unitCosts.get(buildingType);
    }
}
