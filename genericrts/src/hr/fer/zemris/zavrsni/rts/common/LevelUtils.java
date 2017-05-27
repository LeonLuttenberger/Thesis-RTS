package hr.fer.zemris.zavrsni.rts.common;

import hr.fer.zemris.zavrsni.rts.common.map.ITiledMap;
import hr.fer.zemris.zavrsni.rts.common.map.MapTile;

public final class LevelUtils {

    private LevelUtils() {}

    public static boolean canMoveNorth(ITiledMap level, MapTile position) {
        if (position.y >= level.getHeight() - 1) return false;
        if (level.getTileModifier(position.x, position.y + 1) <= 0) return false;
        return true;
    }

    public static boolean canMoveSouth(ITiledMap level, MapTile position) {
        if (position.y <= 0) return false;
        if (level.getTileModifier(position.x, position.y - 1) <= 0) return false;
        return true;
    }

    public static boolean canMoveEast(ITiledMap level, MapTile position) {
        if (position.x >= level.getWidth()) return false;
        if (level.getTileModifier(position.x + 1, position.y) <= 0) return false;
        return true;
    }

    public static boolean canMoveWest(ITiledMap level, MapTile position) {
        if (position.x <= 0) return false;
        if (level.getTileModifier(position.x - 1, position.y) <= 0) return false;
        return true;
    }
}
