package hr.fer.zemris.zavrsni.rts.pathfinding.imp;

import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.common.MapTile;
import hr.fer.zemris.zavrsni.rts.pathfinding.ISearchAgent;

import java.util.Objects;
import java.util.function.Function;

public final class SearchAgentProvider {

    private SearchAgentProvider() {}

    private static Function<ILevel, ISearchAgent<MapTile>> searchAgentConstructor = RTAAStarMapSearchAgent::new;

    public static ISearchAgent<MapTile> getSearchAgent(ILevel level) {
        return searchAgentConstructor.apply(level);
    }

    public static void setSearchAgentConstructor(Function<ILevel, ISearchAgent<MapTile>> searchAgentConstructor) {
        SearchAgentProvider.searchAgentConstructor = Objects.requireNonNull(searchAgentConstructor);
    }
}
