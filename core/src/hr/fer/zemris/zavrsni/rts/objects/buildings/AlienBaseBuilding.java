package hr.fer.zemris.zavrsni.rts.objects.buildings;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.units.HostileUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.IBuildableUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.hostile.AlienSoldierUnit;
import hr.fer.zemris.zavrsni.rts.pathfinding.tiled.map.MapTile;

public class AlienBaseBuilding extends HostileBuilding {

    private static final long serialVersionUID = -2856998936677127338L;

    private static final int WIDTH = 96;
    private static final int HEIGHT = 64;

    private static final int MAX_HIT_POINTS = 300;
    private static final int TRAINING_INCREMENT = 1;
    private static final int MOB_CAP_RANGE = 200;
    private static final int MOB_CAP = 5;

    private int currentWork;
    private IBuildableUnit currentUnit;

    public AlienBaseBuilding(ILevel level) {
        super(level, WIDTH, HEIGHT, MAX_HIT_POINTS);
    }

    @Override
    public TextureRegion loadTexture() {
        return Assets.getInstance().getBuildings().alienBase;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isCapSatisfied()) {
            return;
        }

        if (currentUnit == null) {
            currentUnit = startBuildingUnit();
        }

        currentWork += TRAINING_INCREMENT;
        if (currentWork >= currentUnit.getTrainingCost()) {
            spawnUnit(currentUnit);

            currentWork = 0;
            currentUnit = startBuildingUnit();
        }
    }

    private IBuildableUnit startBuildingUnit() {
        return new AlienSoldierUnit(level);
    }

    private boolean isCapSatisfied() {
        int count = 0;

        for (HostileUnit unit : level.getHostileUnits()) {
            if (AbstractGameObject.distanceBetween(this, unit) < MOB_CAP_RANGE) {
                count++;
            }
        }

        return count >= MOB_CAP;
    }

    private void spawnUnit(IBuildableUnit buildableUnit) {
        if (!(buildableUnit instanceof HostileUnit)) {
            throw new UnsupportedOperationException("Base only build alien units.");
        }

        MapTile spawnPosition = level.getTileForPosition(position.x + dimension.x/2, position.y - 1);

        HostileUnit unit = (HostileUnit) buildableUnit;
        unit.position.x = level.getTileWidth() * spawnPosition.x;
        unit.position.y = level.getTileHeight() * spawnPosition.y;

        level.addHostileUnit(unit);
    }
}
