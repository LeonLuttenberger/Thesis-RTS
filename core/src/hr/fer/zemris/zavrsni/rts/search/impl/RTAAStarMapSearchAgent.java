package hr.fer.zemris.zavrsni.rts.search.impl;

import com.badlogic.gdx.Gdx;
import hr.fer.zemris.zavrsni.rts.search.ISearchAgent;
import hr.fer.zemris.zavrsni.rts.search.SearchNode;
import hr.fer.zemris.zavrsni.rts.search.SearchResult;
import hr.fer.zemris.zavrsni.rts.search.algorithms.AStarSearch;
import hr.fer.zemris.zavrsni.rts.search.algorithms.ISearchAlgorithm;
import hr.fer.zemris.zavrsni.rts.search.heuristic.CachingProblemHeuristic;
import hr.fer.zemris.zavrsni.rts.search.heuristic.IHeuristic;
import hr.fer.zemris.zavrsni.rts.search.heuristic.WeightedHeuristic;
import hr.fer.zemris.zavrsni.rts.search.problem.RTAAStarProblem;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public class RTAAStarMapSearchAgent implements ISearchAgent<MapPosition> {

    private static final String TAG = RTAAStarMapSearchAgent.class.getName();

    private static final IHeuristic<MapPosition> HEURISTIC = new CachingProblemHeuristic<>(new WeightedHeuristic<>(
            new ArealDistanceHeuristic(), 2));
    private static final ISearchAlgorithm<MapPosition> A_STAR_SEARCH = new AStarSearch<>();

    private static final int MAX_STATES_TO_EXPAND = 50;
    private static final int MAX_MOVES = 10;

    private MapPosition currentPosition;
    private MapPosition goalPosition;
    private SearchNode<MapPosition> currentState;
    private RTAAStarProblem<MapPosition, MapPathFindingProblem> searchProblem;
    private SearchResult<MapPosition> searchResult;
    private int movesMade;

    private final ILevel level;

    public RTAAStarMapSearchAgent(ILevel level) {
        this.level = level;
    }

    @Override
    public void pathfind(MapPosition startState, MapPosition goalState) {
        this.currentPosition = startState;
        this.goalPosition = goalState;

        searchResult = null;
        searchProblem = new RTAAStarProblem<>(
                new MapPathFindingProblem(startState, goalState, level),
                MAX_STATES_TO_EXPAND
        );

        Gdx.app.log(TAG, "Set goal to: " + goalState);
    }

    @Override
    public MapPosition update(MapPosition currentPosition) {
        MapPosition previousPosition = this.currentPosition;
        this.currentPosition = currentPosition;

        if (!previousPosition.equals(currentPosition)) {
            movesMade++;
        }

        if (searchResult != null && !searchResult.getStatesQueue().isEmpty()) {
            if (searchResult.getStatesQueue().peek().getState().equals(currentPosition)) {
                searchResult.getStatesQueue().poll();
            }
        }

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

        return searchResult.getStatesQueue().peek().getState();
    }

    private void checkForChanges() {
        for (SearchNode<MapPosition> stateOnPath : searchResult.getStatesQueue()) {
            MapPosition positionOnPath = stateOnPath.getState();

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

            for (SearchNode<MapPosition> searchNode : searchResult.getClosedSet()) {
                double f = currentStateCost + currentStateHeuristic;
                double correctedHeuristic = f - searchNode.getCost();
                searchProblem.cacheHeuristic(searchNode.getState(), correctedHeuristic);
            }
        }

        searchProblem.setNewStartState(currentPosition);
        searchResult = A_STAR_SEARCH.search(searchProblem, HEURISTIC);
        movesMade = 0;

        Gdx.app.log(TAG, "Path found after " + searchResult.getClosedSet().size() + " expanded states.");
    }

    @Override
    public void stopSearch() {
        searchResult = null;
        searchProblem = null;
    }
}
