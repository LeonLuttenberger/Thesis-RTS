package hr.fer.zemris.zavrsni.rts.util;

import hr.fer.zemris.zavrsni.rts.pathfinding.impl.MapTile;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public final class LevelUtils {

    private LevelUtils() {}

    public static boolean canMoveNorth(ILevel level, MapTile position) {
        if (position.y >= level.getHeight() - 1) return false;
        if (level.getTileModifier(position.x, position.y + 1) <= 0) return false;
        return true;
    }

    public static boolean canMoveSouth(ILevel level, MapTile position) {
        if (position.y <= 0) return false;
        if (level.getTileModifier(position.x, position.y - 1) <= 0) return false;
        return true;
    }

    public static boolean canMoveEast(ILevel level, MapTile position) {
        if (position.x >= level.getWidth()) return false;
        if (level.getTileModifier(position.x + 1, position.y) <= 0) return false;
        return true;
    }

    public static boolean canMoveWest(ILevel level, MapTile position) {
        if (position.x <= 0) return false;
        if (level.getTileModifier(position.x - 1, position.y) <= 0) return false;
        return true;
    }

    public static MapTile getMapTile(ILevel level, float x, float y) {
        return new MapTile(
                (int) (x / level.getTileWidth()),
                (int) (y / level.getTileHeight())
        );
    }
}