package hr.fer.zemris.zavrsni.rts.pathfinding.impl;

import hr.fer.zemris.zavrsni.rts.common.map.ITiledMap;
import hr.fer.zemris.zavrsni.rts.common.map.MapTile;
import hr.fer.zemris.zavrsni.rts.pathfinding.ISearchAgent;
import hr.fer.zemris.zavrsni.rts.pathfinding.heuristic.IHeuristic;
import hr.fer.zemris.zavrsni.rts.pathfinding.heuristic.WeightedHeuristic;
import hr.fer.zemris.zavrsni.rts.pathfinding.impl.heuristic.ArealDistanceHeuristic;

import java.util.Objects;
import java.util.function.Function;

public final class SearchAgentProvider {

    private SearchAgentProvider() {}

    private static final int DEFAULT_MAX_STATES_TO_EXPAND = 200;
    private static final int DEFAULT_MAX_MOVES = 20;
    private static final IHeuristic<MapTile> DEFAULT_HEURISTIC = new WeightedHeuristic<>(
            new ArealDistanceHeuristic(), 1.5);

    private static Function<ITiledMap, ISearchAgent<MapTile>> searchAgentConstructor
            = m -> new RTAAStarMapSearchAgent(m, DEFAULT_MAX_STATES_TO_EXPAND, DEFAULT_MAX_MOVES, DEFAULT_HEURISTIC);

    public static ISearchAgent<MapTile> getSearchAgent(ITiledMap level) {
        return searchAgentConstructor.apply(level);
    }

    public static void setSearchAgentConstructor(Function<ITiledMap, ISearchAgent<MapTile>> searchAgentConstructor) {
        SearchAgentProvider.searchAgentConstructor = Objects.requireNonNull(searchAgentConstructor);
    }
}
