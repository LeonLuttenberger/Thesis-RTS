package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import hr.fer.zemris.zavrsni.rts.IUpdatable;
import hr.fer.zemris.zavrsni.rts.objects.units.SimpleUnit;
import hr.fer.zemris.zavrsni.rts.search.ISearchProblem;
import hr.fer.zemris.zavrsni.rts.search.Transition;
import hr.fer.zemris.zavrsni.rts.search.algorithms.AStarSearch;
import hr.fer.zemris.zavrsni.rts.search.algorithms.AbstractSearchAlgorithm;
import hr.fer.zemris.zavrsni.rts.search.impl.ArealDistanceHeuristic;
import hr.fer.zemris.zavrsni.rts.search.impl.MapPathFindingProblem;
import hr.fer.zemris.zavrsni.rts.search.impl.MapPosition;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class PathFindingController implements IUpdatable {

    private Map<SimpleUnit, SearchResult> routes = new HashMap<>();
    private Level level;

    private AbstractSearchAlgorithm<MapPosition> searchAlgorithm = new AStarSearch<>(new ArealDistanceHeuristic());

    public PathFindingController(Level level) {
        this.level = level;
    }

    public void moveUnitsToLocation(List<SimpleUnit> units, Vector3 destination) {
        MapPosition goalTile = getTile(destination);
        Vector3 startPosition = new Vector3();

//        System.out.println("Goal position: " + destination);
//        System.out.println("Goal tile: " + goalTile);

        for (SimpleUnit unit : units) {
            startPosition.set(unit.getCenterX(), unit.getCenterY(), 0);
            MapPosition startTile = getTile(startPosition);
//            System.out.println("Start position: " + startPosition);
//            System.out.println("Start tile: " + startTile);

            ISearchProblem<MapPosition> problem = new MapPathFindingProblem(startTile, goalTile, level);
            List<Transition> transitions = searchAlgorithm.search(problem);

            if (transitions == null) {
                continue;
            } else if (transitions instanceof LinkedList) {
                routes.put(unit, new SearchResult((LinkedList<Transition>) transitions, destination));
            } else {
                routes.put(unit, new SearchResult(new LinkedList<>(transitions), destination));
            }

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
        Vector3 copy = new Vector3(position);
        copy.mul(invIsoTransform);

        int tileX = (int) (copy.x / level.getTileWidth());
        int tileY = (int) (copy.y / level.getTileHeight());

        return new MapPosition(tileX, tileY);
    }

    private static class SearchResult {
        final Queue<Transition> transitions;
        final Vector3 goalPosition;

        SearchResult(Queue<Transition> transitions, Vector3 goalPosition) {
            this.transitions = transitions;
            this.goalPosition = goalPosition;
        }
    }

    @Override
    public void update(float deltaTime) {

    }
}
