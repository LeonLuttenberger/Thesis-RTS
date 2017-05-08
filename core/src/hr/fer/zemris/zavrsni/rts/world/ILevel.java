package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import hr.fer.zemris.zavrsni.rts.IUpdatable;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.resources.Resource;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;

import java.util.List;

public interface ILevel extends IUpdatable {

    void render(SpriteBatch batch);

    List<Unit> getUnits();

    void addUnit(Unit unit);

    void removeUnit(Unit unit);

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
}
