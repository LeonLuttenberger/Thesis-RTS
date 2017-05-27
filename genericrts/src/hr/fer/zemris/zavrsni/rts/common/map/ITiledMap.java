package hr.fer.zemris.zavrsni.rts.common.map;

public interface ITiledMap {

    float getTileModifier(int x, int y);

    int getWidth();

    int getHeight();
}
