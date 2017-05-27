package hr.fer.zemris.zavrsni.rts.common;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import hr.fer.zemris.zavrsni.rts.common.map.AbstractTiledLevel;
import hr.fer.zemris.zavrsni.rts.common.map.TiledMapUtils;
import hr.fer.zemris.zavrsni.rts.objects.buildings.AlienBaseBuilding;
import hr.fer.zemris.zavrsni.rts.objects.buildings.BaseBuilding;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.resources.Resource;
import hr.fer.zemris.zavrsni.rts.objects.resources.ResourceBoulder;
import hr.fer.zemris.zavrsni.rts.objects.units.player.WorkerUnit;

public class Level extends AbstractTiledLevel {

    private static final long serialVersionUID = 439596673197990142L;

    private static final String OBJECT_LAYER = "Object Layer";
    private static final String RESOURCE_TILE_SET = "Objects";
    private static final String TYPE_ID = "type_id";

    public Level(String mapFileName) {
        super(mapFileName);

        initDefaultMap();
    }

    private void initDefaultMap() {
        MapLayer layer = tiledMap.getLayers().get(OBJECT_LAYER);
        TiledMapTileSet objectTileSet = tiledMap.getTileSets().getTileSet(RESOURCE_TILE_SET);

        for (MapObject mapObject : layer.getObjects()) {
            TiledMapTile tile = TiledMapUtils.getTileForObject(mapObject, objectTileSet);
            String typeID = tile.getProperties().get(TYPE_ID, String.class);

            process(mapObject, typeID);
        }
    }

    private void process(MapObject mapObject, String typeID) {
        float positionX = mapObject.getProperties().get("x", Float.class);
        float positionY = mapObject.getProperties().get("y", Float.class);

        switch (typeID) {
            case "boulder":
                Resource rock = new ResourceBoulder(this);
                rock.position.set(positionX, positionY);
                addResource(rock);
                break;

            case "player_base":
                Building playerBase = new BaseBuilding(this);
                playerBase.position.set(positionX, positionY);
                spawnDefaultUnits(playerBase);
                addBuilding(playerBase);
                break;

            case "enemy_base":
                Building alienBase = new AlienBaseBuilding(this);
                alienBase.position.set(positionX, positionY);
                addBuilding(alienBase);
                break;
        }
    }

    private void spawnDefaultUnits(Building building) {
        WorkerUnit worker = new WorkerUnit(this);
        worker.position.set(building.position.x - tileWidth, building.position.y);

        addPlayerUnit(worker);
    }

    @Override
    public void reset() {
        super.reset();

        initDefaultMap();
    }
}
