package hr.fer.zemris.zavrsni.rts.objects.buildings;

import hr.fer.zemris.zavrsni.rts.common.GameState;

import java.util.HashMap;
import java.util.Map;

public final class BuildingCosts {

    public BuildingCosts() {}

    private static final Map<Class<? extends Building>, Cost> buildingCosts = new HashMap<>();

    static {
        buildingCosts.put(TurretBuilding.class, new Cost(300));
    }

    public static Cost getCostFor(Class<? extends Building> buildingType) {
        return buildingCosts.get(buildingType);
    }

    public static final class Cost {
        public final int minerals;

        public Cost(int minerals) {
            this.minerals = minerals;
        }

        public boolean isSatisfied(GameState gameState) {
            return gameState.getResources("minerals") >= minerals;
        }

        public void apply(GameState gameState) {
            gameState.removeResource("minerals", minerals);
        }
    }
}
