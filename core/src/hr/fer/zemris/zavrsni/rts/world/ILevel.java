package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.resources.Resource;
import hr.fer.zemris.zavrsni.rts.objects.units.hostile.HostileUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.player.PlayerUnit;

import java.util.List;

public interface ILevel {

    void render(SpriteBatch batch);

    List<PlayerUnit> getPlayerUnits();

    List<HostileUnit> getHostileUnits();

    void addPlayerUnit(PlayerUnit unit);

    void addHostileUnit(HostileUnit unit);

    void removePlayerUnit(PlayerUnit unit);

    void removeHostileUnit(HostileUnit unit);

    List<Building> getBuildings();

    void addBuilding(Building building);

    void removeBuilding(Building building);

    List<Resource> getResources();

    void addResource(Resource resource);

    void removeResource(Resource resource);

    float getTileModifier(int x, int y);

    float getTerrainModifier(float x, float y);

    AbstractGameObject getObjectOnTile(int x, int y);

    int getWidth();

    int getHeight();

    int getTileWidth();

    int getTileHeight();

    void reset();
}
