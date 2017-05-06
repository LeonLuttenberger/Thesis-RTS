package hr.fer.zemris.zavrsni.rts.world.controllers;

import com.badlogic.gdx.math.Vector2;
import hr.fer.zemris.zavrsni.rts.IUpdatable;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;
import hr.fer.zemris.zavrsni.rts.search.algorithms.AStarSearch;
import hr.fer.zemris.zavrsni.rts.search.algorithms.AbstractSearchAlgorithm;
import hr.fer.zemris.zavrsni.rts.search.impl.ArealDistanceHeuristic;
import hr.fer.zemris.zavrsni.rts.search.impl.MapPosition;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

import java.util.List;

public class PathFindingController implements IUpdatable {

    private ILevel level;

    private AbstractSearchAlgorithm<MapPosition> searchAlgorithm = new AStarSearch<>(new ArealDistanceHeuristic());

    public PathFindingController(ILevel level) {
        this.level = level;
    }

    public void moveUnitsToLocation(List<Unit> units, Vector2 destination) {
        for (Unit unit : units) {
            unit.goToLocation(destination.x, destination.y);
        }
    }

    @Override
    public void update(float deltaTime) {
        // move units

    }
}
