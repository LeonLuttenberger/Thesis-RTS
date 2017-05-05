package hr.fer.zemris.zavrsni.rts.world.controllers;

import com.badlogic.gdx.math.Vector2;
import hr.fer.zemris.zavrsni.rts.IUpdatable;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;
import hr.fer.zemris.zavrsni.rts.search.ISearchProblem;
import hr.fer.zemris.zavrsni.rts.search.Transition;
import hr.fer.zemris.zavrsni.rts.search.algorithms.AStarSearch;
import hr.fer.zemris.zavrsni.rts.search.algorithms.AbstractSearchAlgorithm;
import hr.fer.zemris.zavrsni.rts.search.impl.ArealDistanceHeuristic;
import hr.fer.zemris.zavrsni.rts.search.impl.MapPathFindingProblem;
import hr.fer.zemris.zavrsni.rts.search.impl.MapPosition;
import hr.fer.zemris.zavrsni.rts.util.Vector2Pool;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

import java.util.List;

public class PathFindingController implements IUpdatable {

    private ILevel level;

    private AbstractSearchAlgorithm<MapPosition> searchAlgorithm = new AStarSearch<>(new ArealDistanceHeuristic());

    public PathFindingController(ILevel level) {
        this.level = level;
    }

    public void moveUnitsToLocation(List<Unit> units, Vector2 destination) {
        MapPosition goalTile = getTile(destination);
        Vector2 startPosition = Vector2Pool.getInstance().obtain();

        for (Unit unit : units) {
            startPosition.set(unit.getCenterX(), unit.getCenterY());
            MapPosition startTile = getTile(startPosition);

            ISearchProblem<MapPosition> problem = new MapPathFindingProblem(startTile, goalTile, level);
            List<Transition> transitions = searchAlgorithm.search(problem);

            unit.clearWaypoints();
            if (transitions == null) continue;

            Vector2 currentPosition = startPosition;
            for (Transition transition : transitions) {
                currentPosition = new Vector2(
                        currentPosition.x + transition.dx * level.getTileWidth(),
                        currentPosition.y + transition.dy * level.getTileHeight()
                );
                unit.addWaypoint(currentPosition);
            }
            unit.addWaypoint(destination);
        }

        Vector2Pool.getInstance().free(startPosition);
    }

    private MapPosition getTile(Vector2 position) {
        int tileX = (int) (position.x / level.getTileWidth());
        int tileY = (int) (position.y / level.getTileHeight());

        return new MapPosition(tileX, tileY);
    }

    @Override
    public void update(float deltaTime) {
        // move units
        for (Unit unit : level.getUnits()) {
            if (!unit.hasWaypoint()) continue;

            Vector2 position = Vector2Pool.getInstance().obtain().set(unit.getCenterX(), unit.getCenterY());
            float tileModifier = level.getTerrainModifier(position.x, position.y);

            unit.moveTowardsWaypoint(unit.getMaxSpeed() * tileModifier);
        }
    }
}
