package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.resources.Resource;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;
import hr.fer.zemris.zavrsni.rts.objects.units.hostile.HostileUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.player.PlayerUnit;
import hr.fer.zemris.zavrsni.rts.util.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Level implements ILevel {

    private final List<PlayerUnit> playerUnits = new ArrayList<>();
    private final List<HostileUnit> hostileUnits = new ArrayList<>();
    private final List<Building> buildings = new ArrayList<>();
    private final List<Resource> resources = new ArrayList<>();
    private final AbstractGameObject[][] objectMap;

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
        objectMap = new AbstractGameObject[width][height];

        TiledMapTileLayer mapLayer = (TiledMapTileLayer) tiledMap.getLayers().get(Constants.TERRAIN_LAYER);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                defaultTileModifiers[i][j] = mapLayer.getCell(i, j).getTile().getProperties().get("modifier", Float.class);
            }
        }

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

    @Override
    public void render(SpriteBatch batch) {
        for (Resource resource : resources) {
            resource.render(batch);
        }

        for (Building building : buildings) {
            building.render(batch);
        }

        for (Unit unit : playerUnits) {
            unit.render(batch);
        }

        for (HostileUnit unit : hostileUnits) {
            unit.render(batch);
        }
    }

    @Override
    public List<PlayerUnit> getPlayerUnits() {
        return Collections.unmodifiableList(playerUnits);
    }

    @Override
    public List<HostileUnit> getHostileUnits() {
        return Collections.unmodifiableList(hostileUnits);
    }

    @Override
    public void addPlayerUnit(PlayerUnit unit) {
        playerUnits.add(unit);
    }

    @Override
    public void addHostileUnit(HostileUnit unit) {
        hostileUnits.add(unit);
    }

    @Override
    public void removePlayerUnit(PlayerUnit unit) {
        playerUnits.remove(unit);
    }

    @Override
    public void removeHostileUnit(HostileUnit unit) {
        hostileUnits.remove(unit);
    }

    @Override
    public List<Building> getBuildings() {
        return Collections.unmodifiableList(buildings);
    }

    private void setAdditionalTileModifier(AbstractGameObject object, float value, boolean isAdded) {
        int xTileStart = (int) (object.getPosition().x / tileWidth);
        int yTileStart = (int) (object.getPosition().y / tileHeight);
        int xTileEnd = (int) ((object.getPosition().x + object.getDimension().x - 1) / tileWidth);
        int yTileEnd = (int) ((object.getPosition().y + object.getDimension().y - 1) / tileHeight);

        for (int i = xTileStart; i <= xTileEnd; i++) {
            for (int j = yTileStart; j <= yTileEnd; j++) {
                additionalTileModifiers[i][j] = value;
                objectMap[i][j] = isAdded ? object : null;
            }
        }
    }

    @Override
    public void addBuilding(Building building) {
        buildings.add(building);
        setAdditionalTileModifier(building, 0, true);
    }

    @Override
    public void removeBuilding(Building building) {
        buildings.remove(building);
        setAdditionalTileModifier(building, 1, false);
    }

    @Override
    public List<Resource> getResources() {
        return Collections.unmodifiableList(resources);
    }

    @Override
    public void addResource(Resource resource) {
        resources.add(resource);
        setAdditionalTileModifier(resource, resource.getTerrainModifier(), true);
    }

    @Override
    public void removeResource(Resource resource) {
        resources.remove(resource);
        setAdditionalTileModifier(resource, 1, false);
    }

    @Override
    public float getTileModifier(int x, int y) {
        if (x < 0 || x >= width) return 0;
        if (y < 0 || y >= height) return 0;

        return defaultTileModifiers[x][y] * additionalTileModifiers[x][y];
    }

    @Override
    public float getTerrainModifier(float x, float y) {
        int tileX = (int) ((x - tileWidth / 2) / tileWidth);
        int tileY = (int) ((y - tileHeight / 2) / tileHeight);

        int remainderX = (int) ((x - tileWidth / 2) % tileWidth);
        int remainderY = (int) ((y - tileHeight / 2) % tileHeight);

        // D C
        // A B
        float modifierA = getTileModifier(tileX, tileY);
        float modifierB = getTileModifier(tileX + 1, tileY);
        float modifierC = getTileModifier(tileX + 1, tileY + 1);
        float modifierD = getTileModifier(tileX, tileY + 1);

        float weightA = ((tileWidth - remainderX) * (tileHeight - remainderY)) / (float) (tileWidth * tileHeight);
        float weightB = (remainderX * (tileHeight - remainderY)) / (float) (tileWidth * tileHeight);
        float weightC = (remainderX * remainderY) / (float) (tileWidth * tileHeight);
        float weightD = ((tileWidth - remainderX) * remainderY) / (float) (tileWidth * tileHeight);

        return weightA * modifierA + weightB * modifierB + weightC * modifierC + weightD * modifierD;
    }

    @Override
    public AbstractGameObject getObjectOnTile(int x, int y) {
        return objectMap[x][y];
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getTileWidth() {
        return tileWidth;
    }

    @Override
    public int getTileHeight() {
        return tileHeight;
    }
}
