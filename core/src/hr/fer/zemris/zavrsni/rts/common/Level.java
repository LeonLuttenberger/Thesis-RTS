package hr.fer.zemris.zavrsni.rts.common;

import com.badlogic.gdx.maps.tiled.TiledMap;
import hr.fer.zemris.zavrsni.rts.objects.buildings.BaseBuilding;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.resources.ResourceBoulder;
import hr.fer.zemris.zavrsni.rts.objects.units.player.SoldierUnit;

public class Level extends AbstractTiledLevel {

    private static final String PROP_KEY_BASE_X = "location_base_x";
    private static final String PROP_KEY_BASE_Y = "location_base_y";
    private static final String PROP_KEY_ROCKS = "rocks";

    public Level(TiledMap tiledMap) {
        super(tiledMap);

        initDefaultMap();
    }

    private void initDefaultMap() {
        int baseLocationX = tiledMap.getProperties().get(PROP_KEY_BASE_X, Integer.class);
        int baseLocationY = height - tiledMap.getProperties().get(PROP_KEY_BASE_Y, Integer.class);

        Building base = new BaseBuilding(this);
        base.setCenterX(baseLocationX * tileWidth + tileWidth / 2f);
        base.setCenterY(baseLocationY * tileHeight + tileHeight / 2f);

        base.addHitPoints(base.getMaxHitPoints());
        addBuilding(base);

        spawnDefaultUnits(base);
        spawnRocks();
    }

    private void spawnDefaultUnits(Building building) {
        SoldierUnit soldier = new SoldierUnit(this);
        soldier.position.set(building.position.x - tileWidth, building.position.y);

        addPlayerUnit(soldier);
    }

    private void spawnRocks() {
        String rockLocations = tiledMap.getProperties().get(PROP_KEY_ROCKS, String.class);

        for (String xyPair : rockLocations.split(";")) {
            String[] xyPairArray = xyPair.split(",");

            int rockX = Integer.parseInt(xyPairArray[0]);
            int rockY = height - Integer.parseInt(xyPairArray[1]);

            ResourceBoulder rock = new ResourceBoulder(this);
            rock.position.set(rockX * tileWidth, rockY * tileHeight);
            addResource(rock);
        }
    }

    @Override
    public void reset() {
        super.reset();

        initDefaultMap();
    }
}
