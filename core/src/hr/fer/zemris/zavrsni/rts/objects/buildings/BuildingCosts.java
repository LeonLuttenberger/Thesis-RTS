package hr.fer.zemris.zavrsni.rts.objects.buildings;

import hr.fer.zemris.zavrsni.rts.world.IGameState;

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

        public boolean isSatisfied(IGameState gameState) {
            return gameState.getMinerals() >= minerals;
        }

        public void apply(IGameState gameState) {
            gameState.removeMinerals(minerals);
        }
    }
}
