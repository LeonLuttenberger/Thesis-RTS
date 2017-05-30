package hr.fer.zemris.zavrsni.rts.pathfinding.tiled.map;

public interface ITiledMap {

    float getTileModifier(int x, int y);

    int getWidth();

    int getHeight();
}
