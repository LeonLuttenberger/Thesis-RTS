package hr.fer.zemris.zavrsni.rts.pathfinding.tiled.map;

import java.io.Serializable;

public class MapTile implements Serializable {

    private static final long serialVersionUID = -2285809706571421189L;

    public final int x;
    public final int y;

    public MapTile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapTile that = (MapTile) o;

        if (x != that.x) return false;
        return y == that.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + ",  " + y + ")";
    }
}
