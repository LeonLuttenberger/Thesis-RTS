package hr.fer.zemris.zavrsni.rts.world.controllers;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import hr.fer.zemris.zavrsni.rts.IUpdatable;
import hr.fer.zemris.zavrsni.rts.world.IGameState;

public interface IWorldController extends Disposable, IUpdatable {

    IGameState getGameState();

    void selectUnitsInArea(Vector3 areaStart, Vector3 areaEnd);

    void sendSelectedUnitsTo(Vector3 destination);

    void pause();

    void resume();
}
