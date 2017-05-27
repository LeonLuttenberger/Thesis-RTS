package hr.fer.zemris.zavrsni.rts.common;

import hr.fer.zemris.zavrsni.rts.objects.buildings.AlienBaseBuilding;
import hr.fer.zemris.zavrsni.rts.objects.buildings.BaseBuilding;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.resources.ResourceBoulder;
import hr.fer.zemris.zavrsni.rts.objects.units.player.WorkerUnit;

import java.util.ArrayList;
import java.util.List;

public class Level extends AbstractTiledLevel {

    private static final long serialVersionUID = 439596673197990142L;

    private static final String PROP_KEY_BASE = "player_base";
    private static final String PROP_KEY_ENEMY_BASE = "enemy_bases";
    private static final String PROP_KEY_ROCKS = "rocks";

    public Level(String mapFileName) {
        super(mapFileName);

        initDefaultMap();
    }

    private List<MapTile> parseLocationProperty(String key) {
        List<MapTile> tiles = new ArrayList<>();
        String locations = tiledMap.getProperties().get(key, String.class);

        for (String xyPair : locations.split(";")) {
            String[] xyPairArray = xyPair.split(",");

            int tileX = Integer.parseInt(xyPairArray[0]);
            int tileY = height - Integer.parseInt(xyPairArray[1]);

            tiles.add(new MapTile(tileX, tileY));
        }

        return tiles;
    }

    private void initDefaultMap() {
        initPlayerBase();
        initEnemyBases();
        spawnRocks();
    }

    private void initPlayerBase() {
        List<MapTile> mapTiles = parseLocationProperty(PROP_KEY_BASE);
        if (mapTiles.size() != 1) {
            throw new IllegalArgumentException("Map requires exactly one player base.");
        }

        MapTile tile = mapTiles.get(0);
        Building base = new BaseBuilding(this);
        base.position.x = tile.x * tileWidth;
        base.position.y = tile.y * tileHeight;

        base.addHitPoints(base.getMaxHitPoints());
        addBuilding(base);

        spawnDefaultUnits(base);
    }

    private void spawnDefaultUnits(Building building) {
        WorkerUnit worker = new WorkerUnit(this);
        worker.position.set(building.position.x - tileWidth, building.position.y);

        addPlayerUnit(worker);
    }

    private void initEnemyBases() {
        for (MapTile tile : parseLocationProperty(PROP_KEY_ENEMY_BASE)) {
            AlienBaseBuilding base = new AlienBaseBuilding(this);

            base.position.x = tile.x * tileWidth;
            base.position.y = tile.y * tileHeight;

            base.addHitPoints(base.getMaxHitPoints());
            addBuilding(base);
        }
    }

    private void spawnRocks() {
        for (MapTile mapTile : parseLocationProperty(PROP_KEY_ROCKS)) {
            ResourceBoulder rock = new ResourceBoulder(this);

            rock.position.x = mapTile.x * tileWidth;
            rock.position.y = mapTile.y * tileHeight;

            addResource(rock);
        }
    }

    @Override
    public void reset() {
        super.reset();

        initDefaultMap();
    }
}
