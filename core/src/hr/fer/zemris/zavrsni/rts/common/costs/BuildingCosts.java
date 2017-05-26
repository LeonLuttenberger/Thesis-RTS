package hr.fer.zemris.zavrsni.rts.common.costs;

import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.buildings.TurretBuilding;

import java.util.HashMap;
import java.util.Map;

public final class BuildingCosts {

    private BuildingCosts() {}

    private static final Map<Class<? extends Building>, Cost> buildingCosts = new HashMap<>();

    static {
        buildingCosts.put(TurretBuilding.class, new Cost(100));
    }

    public static Cost getCostFor(Class<? extends Building> buildingType) {
        return buildingCosts.get(buildingType);
    }
}
