package hr.fer.zemris.zavrsni.rts.world.controllers;

import com.badlogic.gdx.utils.Disposable;
import hr.fer.zemris.zavrsni.rts.IUpdatable;
import hr.fer.zemris.zavrsni.rts.world.GameState;

public interface IWorldController extends Disposable, IUpdatable {

    GameState getGameState();
    PathFindingController getPathFindingController();
}
