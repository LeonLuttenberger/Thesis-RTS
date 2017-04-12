package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import hr.fer.zemris.zavrsni.rts.objects.units.SimpleUnit;
import hr.fer.zemris.zavrsni.rts.util.Constants;

import java.util.Collections;
import java.util.List;

public class Level {

    public static final String TAG = Level.class.getName();

    private List<SimpleUnit> units;

    private final int width;
    private final int height;
    private final float[][] tileModifiers;

    private final int tileWidth;
    private final int tileHeight;

    public Level(TiledMap tiledMap) {
        width = tiledMap.getProperties().get("width", Integer.class);
        height = tiledMap.getProperties().get("height", Integer.class);
        tileModifiers = new float[width][height];

        TiledMapTileLayer mapLayer = (TiledMapTileLayer) tiledMap.getLayers().get(Constants.TERRAIN_LAYER);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tileModifiers[i][j] = mapLayer.getCell(i, j).getTile().getProperties().get("modifier", Float.class);
            }
        }

        units = Collections.singletonList(new SimpleUnit());

        tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);
    }

    public void render(SpriteBatch batch) {
        for (SimpleUnit unit : units) {
            unit.render(batch);
        }
    }

    public List<SimpleUnit> getUnits() {
        return Collections.unmodifiableList(units);
    }

    public float getTileModifier(int x, int y) {
        return tileModifiers[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }
}
