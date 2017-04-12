package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import hr.fer.zemris.zavrsni.rts.objects.units.SimpleUnit;
import hr.fer.zemris.zavrsni.rts.search.ISearchProblem;
import hr.fer.zemris.zavrsni.rts.search.Transition;
import hr.fer.zemris.zavrsni.rts.search.algorithms.AStarSearch;
import hr.fer.zemris.zavrsni.rts.search.algorithms.AbstractSearchAlgorithm;
import hr.fer.zemris.zavrsni.rts.search.impl.ArealDistanceHeuristic;
import hr.fer.zemris.zavrsni.rts.search.impl.MapPathfindingProblem;
import hr.fer.zemris.zavrsni.rts.search.impl.MapPosition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathFindingController {

    private Map<SimpleUnit, SearchResult> routes = new HashMap<>();
    private Level level;

    private AbstractSearchAlgorithm<MapPosition> searchAlgorithm = new AStarSearch<>(new ArealDistanceHeuristic());

    public PathFindingController(Level level) {
        this.level = level;
    }

    public void moveUnitsToLocation(List<SimpleUnit> units, Vector3 destination) {
        MapPosition goalTile = getTile(destination);
        Vector3 startPosition = new Vector3();

        for (SimpleUnit unit : units) {
            startPosition.set(unit.getPosition(), 0);
            MapPosition startTile = getTile(startPosition);

            ISearchProblem<MapPosition> problem = new MapPathfindingProblem(startTile, goalTile, level);
            List<Transition> transitions = searchAlgorithm.search(problem);

            routes.put(unit, new SearchResult(transitions, destination));
        }
    }

    private Matrix4 invIsoTransform;
    {
        Matrix4 isoTransform = new Matrix4();
        isoTransform.idt();
        isoTransform.translate(0, 32, 0);
        isoTransform.scale((float) Math.sqrt(0.5), (float) Math.sqrt(0.5) / 2, 1);
        isoTransform.rotate(0, 0, 1, -45);

        invIsoTransform = new Matrix4(isoTransform.inv());
    }

    private MapPosition getTile(Vector3 position) {
        position.mul(invIsoTransform);

        int tileX = (int) (position.x / level.getTileWidth());
        int tileY = (int) (position.y / level.getTileHeight());

        return new MapPosition(tileX, tileY);
    }

    private static class SearchResult {
        final List<Transition> transitions;
        final Vector3 goalPosition;

        SearchResult(List<Transition> transitions, Vector3 goalPosition) {
            this.transitions = transitions;
            this.goalPosition = goalPosition;
        }
    }
}
