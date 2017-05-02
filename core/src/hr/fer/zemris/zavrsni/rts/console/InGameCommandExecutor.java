package hr.fer.zemris.zavrsni.rts.console;

import com.strongjoshua.console.LogLevel;
import hr.fer.zemris.zavrsni.rts.objects.resources.Resource;
import hr.fer.zemris.zavrsni.rts.objects.resources.ResourceBoulder;
import hr.fer.zemris.zavrsni.rts.objects.units.SimpleUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;
import hr.fer.zemris.zavrsni.rts.util.Constants;
import hr.fer.zemris.zavrsni.rts.world.IGameState;

public class InGameCommandExecutor extends MyRTSCommandExecutor {

    private final IGameState gameState;

    public InGameCommandExecutor(IGameState gameState) {
        this.gameState = gameState;
    }

    public void spawnBoulder(int x, int y) {
        Resource boulder = new ResourceBoulder();
        boulder.getPosition().set(x * Constants.TILE_WIDTH, y * Constants.TILE_HEIGHT);

        gameState.getLevel().addResource(boulder);
        console.log("Spawned boulder at " + x + " " + y, LogLevel.SUCCESS);
    }

    public void spawnSimpleUnit(int x, int y) {
        Unit unit = new SimpleUnit();
        unit.getPosition().set(x * Constants.TILE_WIDTH, y * Constants.TILE_HEIGHT);

        gameState.getLevel().addUnit(unit);
        console.log("Spawned unit at " + x + " " + y, LogLevel.SUCCESS);
    }

    public void mapSize() {
        int width = gameState.getLevel().getWidth();
        int height = gameState.getLevel().getHeight();
        console.log("Map size in tiles is: (" + width + ", " + height + ")", LogLevel.DEFAULT);
    }

    public void resetGame() {
        gameState.reset();
    }
}
