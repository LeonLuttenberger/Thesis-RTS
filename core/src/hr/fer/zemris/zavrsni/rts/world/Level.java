package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.resources.Resource;
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
    private final List<Resource> resources = new ArrayList<>();

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

        if (tileWidth != Constants.TILE_WIDTH) {
            throw new RuntimeException("Tile width must be " + Constants.TILE_WIDTH);
        }
        if (tileHeight != Constants.TILE_HEIGHT) {
            throw new RuntimeException("Tile height must be " + Constants.TILE_HEIGHT);
        }

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

        for (Resource resource : resources) {
            resource.render(batch);
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

    private void setAdditionalTileModifier(AbstractGameObject object, float value) {
        int xTileStart = (int) (object.getPosition().x / tileWidth);
        int yTileStart = (int) (object.getPosition().y / tileHeight);
        int xTileEnd = (int) ((object.getPosition().x + object.getDimension().x) / tileWidth);
        int yTileEnd = (int) ((object.getPosition().y + object.getDimension().y) / tileHeight);

        for (int i = xTileStart; i < xTileEnd; i++) {
            for (int j = yTileStart; j < yTileEnd; j++) {
                additionalTileModifiers[i][j] = value;
            }
        }
    }

    public void addBuilding(Building building) {
        buildings.add(building);
        setAdditionalTileModifier(building, 0);
    }

    public void removeBuilding(Building building) {
        buildings.remove(building);
        setAdditionalTileModifier(building, 1);
    }

    public List<Resource> getResources() {
        return Collections.unmodifiableList(resources);
    }

    public void addResource(Resource resource) {
        resources.add(resource);
        setAdditionalTileModifier(resource, resource.getTerrainModifier());
    }

    public void removeResource(Resource resource) {
        resources.remove(resource);
        setAdditionalTileModifier(resource, 1);
    }

    public float getTileModifier(int x, int y) {
        return defaultTileModifiers[x][y] * additionalTileModifiers[x][y];
    }

    public float getTerrainModifier(float x, float y) {
        return getTileModifier((int) (x / tileWidth), (int) (y / tileHeight));
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
