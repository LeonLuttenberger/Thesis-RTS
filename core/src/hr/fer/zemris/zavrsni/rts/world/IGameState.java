package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.math.Vector3;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;

import java.util.List;

public interface IGameState {

    ILevel getLevel();

    void setLevel(ILevel level);

    void selectUnitsInArea(Vector3 areaStart, Vector3 areaEnd);

    List<Unit> getSelectedUnits();

    void reset();
}
