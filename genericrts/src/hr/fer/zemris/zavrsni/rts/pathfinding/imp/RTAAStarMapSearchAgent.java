package hr.fer.zemris.zavrsni.rts.pathfinding.imp;

import com.badlogic.gdx.Gdx;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.common.MapTile;
import hr.fer.zemris.zavrsni.rts.pathfinding.ISearchAgent;
import hr.fer.zemris.zavrsni.rts.pathfinding.SearchNode;
import hr.fer.zemris.zavrsni.rts.pathfinding.SearchResult;
import hr.fer.zemris.zavrsni.rts.pathfinding.algorithms.AStarSearch;
import hr.fer.zemris.zavrsni.rts.pathfinding.algorithms.ISearchAlgorithm;
import hr.fer.zemris.zavrsni.rts.pathfinding.heuristic.CachingProblemHeuristic;
import hr.fer.zemris.zavrsni.rts.pathfinding.heuristic.IHeuristic;
import hr.fer.zemris.zavrsni.rts.pathfinding.heuristic.WeightedHeuristic;
import hr.fer.zemris.zavrsni.rts.pathfinding.imp.heuristic.ArealDistanceHeuristic;
import hr.fer.zemris.zavrsni.rts.pathfinding.imp.problem.MoveToAdjacentTileProblem;
import hr.fer.zemris.zavrsni.rts.pathfinding.problem.RTAAStarProblem;

import java.util.Map.Entry;

public class RTAAStarMapSearchAgent implements ISearchAgent<MapTile> {

    private static final String TAG = RTAAStarMapSearchAgent.class.getName();

    private static final IHeuristic<MapTile> HEURISTIC = new CachingProblemHeuristic<>(new WeightedHeuristic<>(
            new ArealDistanceHeuristic(), 1.5
    ));
    private static final ISearchAlgorithm<MapTile> A_STAR_SEARCH = new AStarSearch<>();

    private static final int MAX_STATES_TO_EXPAND = 200;
    private static final int MAX_MOVES = 20;

    private MapTile currentPosition;
    private MapTile goalPosition;
    private SearchNode<MapTile> currentState;
    private RTAAStarProblem<MapTile, MoveToAdjacentTileProblem> searchProblem;
    private SearchResult<MapTile> searchResult;
    private int movesMade;

    private final ILevel level;

    public RTAAStarMapSearchAgent(ILevel level) {
        this.level = level;
    }

    @Override
    public void pathfind(MapTile startState, MapTile goalState) {
        this.currentPosition = startState;
        this.goalPosition = goalState;

        searchResult = null;
        searchProblem = new RTAAStarProblem<>(
                new MoveToAdjacentTileProblem(startState, goalState, level),
                MAX_STATES_TO_EXPAND
        );

        Gdx.app.log(TAG, "Set goal to: " + goalState);
    }

    @Override
    public MapTile getAction(MapTile currentPosition) {
        MapTile previousPosition = this.currentPosition;
        this.currentPosition = currentPosition;

        // count the moves made
        if (!previousPosition.equals(currentPosition)) {
            movesMade++;
        }

        // move on to the next state
        if (searchResult != null && !searchResult.getStatesQueue().isEmpty()) {
            if (searchResult.getStatesQueue().peek().getState().equals(currentPosition)) {
                searchResult.getStatesQueue().poll();
            }
        }

        // check if the search has to be re-done
        if (searchResult == null || searchResult.getStatesQueue().isEmpty() || movesMade >= MAX_MOVES) {
            resetSearch();

            if (searchResult == null) {
                Gdx.app.log(TAG, "Could not find path to destination.");
                stopSearch();
                return null;
            }

            currentState = searchResult.getFrontierQueue().peek();
            Gdx.app.log(TAG, "Moving towards " + currentState.getState());
        }

        // check for changes
        checkForChanges();

        if (searchResult.getStatesQueue().size() == 1 &&
                currentPosition.equals(searchResult.getStatesQueue().peek().getState())) {
            return null;
        }

        return searchResult.getStatesQueue().peek().getState();
    }

    private void checkForChanges() {
        for (SearchNode<MapTile> stateOnPath : searchResult.getStatesQueue()) {
            MapTile positionOnPath = stateOnPath.getState();

            float cachedModifier = searchProblem.getProblem().getProblem().getCachedModifier(positionOnPath);
            float currentModifier = level.getTileModifier(positionOnPath.x, positionOnPath.y);
            if (cachedModifier != currentModifier) {
                currentState = searchResult.getStatesQueue().peek();
                searchResult.getStatesQueue().clear();
                resetSearch();
            }
        }
    }

    private void resetSearch() {
        if (searchResult != null) {
            double currentStateCost = currentState.getCost();
            double currentStateHeuristic = currentState.getHeuristic();

            for (Entry<MapTile, Double> entry : searchResult.getClosedSet().entrySet()) {
                double f = currentStateCost + currentStateHeuristic;
                double correctedHeuristic = f - entry.getValue();
                searchProblem.cacheHeuristic(entry.getKey(), correctedHeuristic);
            }
        }

        searchProblem.setStartState(currentPosition);
        searchResult = A_STAR_SEARCH.search(searchProblem, HEURISTIC);
        movesMade = 0;

        Gdx.app.log(TAG, "Path found after " + searchResult.getClosedSet().size() + " expanded states.");
    }

    @Override
    public void stopSearch() {
        searchResult = null;
        searchProblem = null;
    }

    @Override
    public boolean isGoalState(MapTile tile) {
        return searchProblem == null || searchProblem.isGoalState(tile);
    }

    @Override
    public MapTile getGoalState() {
        return goalPosition;
    }
}
