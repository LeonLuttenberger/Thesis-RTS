package hr.fer.zemris.zavrsni.rts.pathfinding.tiled;

import hr.fer.zemris.zavrsni.rts.pathfinding.ISearchAgent;
import hr.fer.zemris.zavrsni.rts.pathfinding.SearchNode;
import hr.fer.zemris.zavrsni.rts.pathfinding.SearchResult;
import hr.fer.zemris.zavrsni.rts.pathfinding.algorithms.AStarSearch;
import hr.fer.zemris.zavrsni.rts.pathfinding.algorithms.ISearchAlgorithm;
import hr.fer.zemris.zavrsni.rts.pathfinding.heuristic.CachingProblemHeuristic;
import hr.fer.zemris.zavrsni.rts.pathfinding.heuristic.IHeuristic;
import hr.fer.zemris.zavrsni.rts.pathfinding.problem.IModifierCachingProblem;
import hr.fer.zemris.zavrsni.rts.pathfinding.problem.RTAAStarProblem;
import hr.fer.zemris.zavrsni.rts.pathfinding.tiled.heuristic.ArealDistanceHeuristic;
import hr.fer.zemris.zavrsni.rts.pathfinding.tiled.map.ITiledMap;
import hr.fer.zemris.zavrsni.rts.pathfinding.tiled.map.MapTile;
import hr.fer.zemris.zavrsni.rts.pathfinding.tiled.problem.MoveToAdjacentTileProblem;
import hr.fer.zemris.zavrsni.rts.pathfinding.tiled.problem.MoveToTileProblem;
import hr.fer.zemris.zavrsni.rts.pathfinding.util.TriFunction;

import java.util.Collections;
import java.util.Map.Entry;

public class RTAAStarMapSearchAgent implements ISearchAgent<MapTile> {

    private static final IHeuristic<MapTile> DEFAULT_HEURISTIC = new ArealDistanceHeuristic();
    private static final TriFunction<MapTile, MapTile, ITiledMap, IModifierCachingProblem<MapTile>> DEFAULT_PROBLEM = MoveToTileProblem::new;
    private static final ISearchAlgorithm<MapTile> A_STAR_SEARCH = new AStarSearch<>();

    private final ITiledMap level;
    private final IHeuristic<MapTile> heuristic;
    private final int maxStatesToExpand;
    private final int maxMoves;

    private TriFunction<MapTile, MapTile, ITiledMap, IModifierCachingProblem<MapTile>> problemFunction = DEFAULT_PROBLEM;

    private MapTile currentPosition;
    private MapTile goalPosition;
    private SearchNode<MapTile> currentState;
    private RTAAStarProblem<MapTile, IModifierCachingProblem<MapTile>> searchProblem;
    private SearchResult<MapTile> searchResult;
    private int movesMade;

    public RTAAStarMapSearchAgent(ITiledMap level, int maxStatesToExpand, int maxMoves, IHeuristic<MapTile> heuristic) {
        this.level = level;
        this.maxStatesToExpand = maxStatesToExpand;
        this.maxMoves = maxMoves;

        this.heuristic = new CachingProblemHeuristic<>(heuristic);
    }

    public RTAAStarMapSearchAgent(ITiledMap level, int maxStatesToExpand, int maxMoves) {
        this(level, maxStatesToExpand, maxMoves, DEFAULT_HEURISTIC);
    }

    @Override
    public void pathfind(MapTile startState, MapTile goalState) {
        this.currentPosition = startState;
        this.goalPosition = goalState;

        IModifierCachingProblem<MapTile> problem = problemFunction.apply(startState, goalState, level);
        if (level.getTileModifier(goalState.x, goalState.y) <= 0) {
            problem = new MoveToAdjacentTileProblem(level, problem);
        }

        searchResult = null;
        searchProblem = new RTAAStarProblem<>(
                problem,
                maxStatesToExpand
        );
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
        if (searchResult == null || searchResult.getStatesQueue().isEmpty() || movesMade >= maxMoves) {
            resetSearch();

            if (searchResult == null) {
                stopSearch();
                return null;
            }

            currentState = searchResult.getFrontierQueue().peek();
        }

        // check for changes
        checkForChanges();

        if (searchResult.getStatesQueue().size() == 1 &&
                currentPosition.equals(searchResult.getStatesQueue().peek().getState())) {
            return null;
        }

        return searchResult.getStatesQueue().peek().getState();
    }

    public void setProblemFunction(TriFunction<MapTile, MapTile, ITiledMap, IModifierCachingProblem<MapTile>> problemFunction) {
        this.problemFunction = problemFunction;
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
            double f = currentStateCost + currentStateHeuristic;

            for (Entry<MapTile, Double> entry : searchResult.getClosedSet().entrySet()) {
                double correctedHeuristic = f - entry.getValue();
                searchProblem.cacheHeuristic(entry.getKey(), correctedHeuristic);
            }
        }

        searchProblem.setStartState(currentPosition);
        searchResult = A_STAR_SEARCH.search(searchProblem, heuristic);
        movesMade = 0;
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

    @Override
    public Iterable<MapTile> getStatesQueue() {
        if (searchResult == null) {
            return Collections.emptyList();
        }

        return searchResult.getStatesQueue().stream().map(SearchNode::getState)::iterator;
    }
}
