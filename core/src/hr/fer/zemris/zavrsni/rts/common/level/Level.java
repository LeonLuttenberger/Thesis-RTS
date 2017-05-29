package hr.fer.zemris.zavrsni.rts.common.level;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import hr.fer.zemris.zavrsni.rts.common.map.AbstractTiledLevel;
import hr.fer.zemris.zavrsni.rts.common.map.TiledMapUtils;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.buildings.BaseBuilding;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.resources.Resource;
import hr.fer.zemris.zavrsni.rts.objects.units.HostileUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.PlayerUnit;
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
        AbstractGameObject object = LevelObjectLoader.getObject(this, mapObject, typeID);
        if (object == null) return;

        if (object instanceof Resource) {
            addResource((Resource) object);
            return;
        }

        if (object instanceof PlayerUnit) {
            addPlayerUnit((PlayerUnit) object);
            return;
        }

        if (object instanceof HostileUnit) {
            addHostileUnit((HostileUnit) object);
            return;
        }

        if (object instanceof Building) {
            addBuilding((Building) object);

            if (object instanceof BaseBuilding) {
                spawnDefaultUnits((BaseBuilding) object);
            }
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
