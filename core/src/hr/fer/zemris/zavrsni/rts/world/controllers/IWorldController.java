package hr.fer.zemris.zavrsni.rts.world.controllers;

import com.badlogic.gdx.utils.Disposable;
import hr.fer.zemris.zavrsni.rts.IUpdatable;
import hr.fer.zemris.zavrsni.rts.world.IGameState;

public interface IWorldController extends Disposable, IUpdatable {

    IGameState getGameState();
    PathFindingController getPathFindingController();
}
