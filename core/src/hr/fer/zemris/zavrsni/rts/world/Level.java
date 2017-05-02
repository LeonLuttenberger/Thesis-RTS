package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.units.SimpleUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;
import hr.fer.zemris.zavrsni.rts.util.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Level {

    public static final String TAG = Level.class.getName();

    private final List<Unit> units = new ArrayList<>();
    private final List<Building> buildings = new ArrayList<>();

    private final int width;
    private final int height;
    private final float[][] defaultTileModifiers;
    private final float[][] additionalTileModifiers;

    private final int tileWidth;
    private final int tileHeight;

    public Level(TiledMap tiledMap) {
        width = tiledMap.getProperties().get("width", Integer.class);
        height = tiledMap.getProperties().get("height", Integer.class);
        defaultTileModifiers = new float[width][height];

        TiledMapTileLayer mapLayer = (TiledMapTileLayer) tiledMap.getLayers().get(Constants.TERRAIN_LAYER);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                defaultTileModifiers[i][j] = mapLayer.getCell(i, j).getTile().getProperties().get("modifier", Float.class);
            }
        }

        units.add(new SimpleUnit());

        tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);

        additionalTileModifiers = new float[width][height];
        for (float[] rowTileModifiers : additionalTileModifiers) {
            Arrays.fill(rowTileModifiers, 1);
        }
    }

    public void render(SpriteBatch batch) {
        for (Unit unit : units) {
            unit.render(batch);
        }

        for (Building building : buildings) {
            building.render(batch);
        }
    }

    public List<Unit> getUnits() {
        return Collections.unmodifiableList(units);
    }

    public void addUnit(Unit unit) {
        units.add(unit);
    }

    public void removeUnit(Unit unit) {
        units.remove(unit);
    }

    public List<Building> getBuildings() {
        return Collections.unmodifiableList(buildings);
    }

    public void addBuilding(Building building) {
        buildings.add(building);

        int xTileStart = (int) (building.getPosition().x / tileWidth);
        int yTileStart = (int) (building.getPosition().y / tileHeight);
        int xTileEnd = (int) ((building.getPosition().x + building.getDimension().x) / tileWidth);
        int yTileEnd = (int) ((building.getPosition().y + building.getDimension().y) / tileHeight);

        for (int i = xTileStart; i < xTileEnd; i++) {
            for (int j = yTileStart; j < yTileEnd; j++) {
                additionalTileModifiers[i][j] = 0;
            }
        }
    }

    public void removeBuilding(Building building) {
        buildings.remove(building);
    }

    public float getTileModifier(int x, int y) {
        return defaultTileModifiers[x][y] * additionalTileModifiers[x][y];
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
