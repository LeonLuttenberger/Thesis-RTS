package hr.fer.zemris.zavrsni.rts.common.map;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.projectiles.Projectile;
import hr.fer.zemris.zavrsni.rts.objects.resources.Resource;
import hr.fer.zemris.zavrsni.rts.objects.units.HostileUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.PlayerUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractTiledLevel implements ILevel {

    private static final long serialVersionUID = 4501866854251924006L;

    private static final String TERRAIN_LAYER = "Terrain Layer";

    private static final String PROP_KEY_WIDTH = "width";
    private static final String PROP_KEY_HEIGHT = "height";
    private static final String PROP_KEY_MODIFIER = "modifier";
    private static final String PROP_KEY_TILE_WIDTH = "tilewidth";
    private static final String PROP_KEY_TILE_HEIGHT = "tileheight";

    private final String mapFileName;
    protected transient TiledMap tiledMap;

    private final List<PlayerUnit> playerUnits = new ArrayList<>();
    private final List<HostileUnit> hostileUnits = new ArrayList<>();
    private final List<Building> buildings = new ArrayList<>();
    private final List<Resource> resources = new ArrayList<>();
    private final List<Projectile> projectiles = new ArrayList<>();
    private final AbstractGameObject[][] objectMap;

    protected final int width;
    protected final int height;
    protected final float[][] defaultTileModifiers;
    protected final float[][] additionalTileModifiers;

    protected final int tileWidth;
    protected final int tileHeight;

    public AbstractTiledLevel(String mapFileName) {
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.textureMagFilter = TextureFilter.Nearest;
        params.textureMinFilter = TextureFilter.Nearest;

        this.tiledMap = new TmxMapLoader().load(mapFileName, params);
        this.mapFileName = mapFileName;

        width = tiledMap.getProperties().get(PROP_KEY_WIDTH, Integer.class);
        height = tiledMap.getProperties().get(PROP_KEY_HEIGHT, Integer.class);

        defaultTileModifiers = new float[width][height];
        objectMap = new AbstractGameObject[width][height];

        TiledMapTileLayer mapLayer = (TiledMapTileLayer) tiledMap.getLayers().get(TERRAIN_LAYER);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                defaultTileModifiers[i][j] = mapLayer.getCell(i, j).getTile().getProperties().get(PROP_KEY_MODIFIER, Float.class);
            }
        }

        tileWidth = tiledMap.getProperties().get(PROP_KEY_TILE_WIDTH, Integer.class);
        tileHeight = tiledMap.getProperties().get(PROP_KEY_TILE_HEIGHT, Integer.class);

        additionalTileModifiers = new float[width][height];
        initTileModifier();
    }

    private void initTileModifier() {
        for (float[] rowTileModifiers : additionalTileModifiers) {
            Arrays.fill(rowTileModifiers, 1);
        }
    }

    public TiledMap getTiledMap() {
        return tiledMap;
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

        for (Projectile projectile : projectiles) {
            projectile.render(batch);
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
    public boolean addPlayerUnit(PlayerUnit unit) {
        if (canPlaceObject(unit)) {
            return playerUnits.add(unit);
        }

        return false;
    }

    @Override
    public boolean addHostileUnit(HostileUnit unit) {
        if (canPlaceObject(unit)) {
            return hostileUnits.add(unit);
        }

        return false;
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
        int xTileStart = (int) (object.position.x / tileWidth);
        int yTileStart = (int) (object.position.y / tileHeight);
        int xTileEnd = (int) ((object.position.x + object.dimension.x - 1) / tileWidth);
        int yTileEnd = (int) ((object.position.y + object.dimension.y - 1) / tileHeight);

        for (int i = xTileStart; i <= xTileEnd; i++) {
            for (int j = yTileStart; j <= yTileEnd; j++) {
                additionalTileModifiers[i][j] = value;
                objectMap[i][j] = isAdded ? object : null;
            }
        }
    }

    @Override
    public boolean addBuilding(Building building) {
        if (canPlaceObject(building)) {
            buildings.add(building);
            setAdditionalTileModifier(building, 0, true);
            return true;
        }

        return false;
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
    public boolean addResource(Resource resource) {
        if (canPlaceObject(resource)) {
            resources.add(resource);
            setAdditionalTileModifier(resource, resource.getTerrainModifier(), true);
            return true;
        }

        return false;
    }

    @Override
    public void removeResource(Resource resource) {
        resources.remove(resource);
        setAdditionalTileModifier(resource, 1, false);
    }

    @Override
    public List<Projectile> getProjectiles() {
        return Collections.unmodifiableList(projectiles);
    }

    @Override
    public boolean addProjectile(Projectile projectile) {
        return projectiles.add(projectile);
    }

    @Override
    public void removeProjectile(Projectile projectile) {
        projectiles.remove(projectile);
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


    private boolean isTerrainValidFor(AbstractGameObject object) {
        int xTileStart = (int) (object.position.x / tileWidth);
        int yTileStart = (int) (object.position.y / tileHeight);
        int xTileEnd = (int) ((object.position.x + object.dimension.x - 1) / tileWidth);
        int yTileEnd = (int) ((object.position.y + object.dimension.y - 1) / tileHeight);

        for (int i = xTileStart; i <= xTileEnd; i++) {
            for (int j = yTileStart; j <= yTileEnd; j++) {
                if (getTileModifier(i, j) == 0) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isUnitInWay(AbstractGameObject object) {
        Iterator<Unit> it = Stream.concat(playerUnits.stream(), hostileUnits.stream()).iterator();

        while (it.hasNext()) {
            Unit unit = it.next();
            if (object.containsPoint(unit.getCenterX(), unit.getCenterY())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean canPlaceObject(AbstractGameObject object) {
        if (object instanceof Unit) {
           return isTerrainValidFor(object);
        }

        if (!isTerrainValidFor(object)) return false;
        if (isUnitInWay(object)) return false;

        return true;
    }

    @Override
    public void reset() {
        playerUnits.clear();
        hostileUnits.clear();
        buildings.clear();
        resources.clear();

        initTileModifier();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        this.tiledMap = new TmxMapLoader().load(mapFileName);
    }
}
