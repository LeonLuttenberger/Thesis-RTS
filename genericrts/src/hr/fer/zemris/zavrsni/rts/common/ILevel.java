package hr.fer.zemris.zavrsni.rts.common;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.projectiles.Projectile;
import hr.fer.zemris.zavrsni.rts.objects.resources.Resource;
import hr.fer.zemris.zavrsni.rts.objects.units.HostileUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.PlayerUnit;
import hr.fer.zemris.zavrsni.rts.pathfinding.tiled.map.ITiledMap;
import hr.fer.zemris.zavrsni.rts.pathfinding.tiled.map.MapTile;

import java.io.Serializable;
import java.util.List;

public interface ILevel extends ITiledMap, Serializable {

    void render(SpriteBatch batch);

    List<PlayerUnit> getPlayerUnits();

    List<HostileUnit> getHostileUnits();

    boolean addPlayerUnit(PlayerUnit unit);

    boolean addHostileUnit(HostileUnit unit);

    void removePlayerUnit(PlayerUnit unit);

    void removeHostileUnit(HostileUnit unit);

    List<Building> getBuildings();

    boolean addBuilding(Building building);

    void removeBuilding(Building building);

    List<Resource> getResources();

    boolean addResource(Resource resource);

    void removeResource(Resource resource);

    List<Projectile> getProjectiles();

    boolean addProjectile(Projectile projectile);

    void removeProjectile(Projectile projectile);

    float getTerrainModifier(float x, float y);

    AbstractGameObject getObjectOnTile(int x, int y);

    int getTileWidth();

    int getTileHeight();

    void reset();

    boolean canPlaceObject(AbstractGameObject object);

    default MapTile getTileForPosition(float x, float y) {
        return new MapTile(
                (int) (x / getTileWidth()),
                (int) (y / getTileHeight())
        );
    }
}
