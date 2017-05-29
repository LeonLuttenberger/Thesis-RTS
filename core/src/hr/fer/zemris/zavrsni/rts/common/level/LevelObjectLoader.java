package hr.fer.zemris.zavrsni.rts.common.level;

import com.badlogic.gdx.maps.MapObject;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.buildings.AlienBaseBuilding;
import hr.fer.zemris.zavrsni.rts.objects.buildings.BaseBuilding;
import hr.fer.zemris.zavrsni.rts.objects.resources.ResourceBoulder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class LevelObjectLoader {

    private LevelObjectLoader() {}

    private static final Map<String, Function<ILevel, AbstractGameObject>> constructorMap = new HashMap<>();

    static {
        constructorMap.put("boulder", ResourceBoulder::new);
        constructorMap.put("player_base", BaseBuilding::new);
        constructorMap.put("enemy_base", AlienBaseBuilding::new);
    }

    public static AbstractGameObject getObject(ILevel level, MapObject mapObject, String typeID) {
        float positionX = mapObject.getProperties().get("x", Float.class);
        float positionY = mapObject.getProperties().get("y", Float.class);

        Function<ILevel, AbstractGameObject> constructor = constructorMap.get(typeID);
        if (constructor == null) return null;

        AbstractGameObject gameObject = constructor.apply(level);
        gameObject.position.set(positionX, positionY);

        return gameObject;
    }
}
