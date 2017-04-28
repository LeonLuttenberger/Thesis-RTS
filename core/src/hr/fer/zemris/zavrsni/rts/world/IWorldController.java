package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.utils.Disposable;
import hr.fer.zemris.zavrsni.rts.IUpdatable;

public interface IWorldController extends Disposable, IUpdatable {

    GameState getGameState();
    PathFindingController getPathFindingController();
}
