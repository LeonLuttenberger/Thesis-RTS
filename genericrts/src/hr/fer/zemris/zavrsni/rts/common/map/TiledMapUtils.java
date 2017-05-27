package hr.fer.zemris.zavrsni.rts.common.map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;

public final class TiledMapUtils {

    public static TiledMapTile getTileForObject(MapObject object, TiledMapTileSet tileSet) {
        int gid = object.getProperties().get("gid", Integer.class);

        return tileSet.getTile(gid);
    }

    private TiledMapUtils() {}
}
