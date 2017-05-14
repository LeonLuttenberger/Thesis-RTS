package hr.fer.zemris.zavrsni.rts.console;

import com.strongjoshua.console.LogLevel;
import hr.fer.zemris.zavrsni.rts.objects.resources.Resource;
import hr.fer.zemris.zavrsni.rts.objects.resources.ResourceBoulder;
import hr.fer.zemris.zavrsni.rts.objects.units.hostile.AlienBugUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.player.SoldierUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.player.WorkerUnit;
import hr.fer.zemris.zavrsni.rts.world.IGameState;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public class InGameCommandExecutor extends MyRTSCommandExecutor {

    private final IGameState gameState;

    public InGameCommandExecutor(IGameState gameState) {
        this.gameState = gameState;
    }

    private boolean isPositionValid(int x, int y) {
        boolean valid = true;

        if (x < 0 || x >= gameState.getLevel().getWidth()) {
            valid = false;
        }
        if (y < 0 || y >= gameState.getLevel().getHeight()) {
            valid = false;
        }

        if (!valid) {
            console.log("Position " + x + " " + y + " is not valid.", LogLevel.ERROR);
        }

        return valid;
    }

    public void spawnBoulder(int x, int y) {
        if (!isPositionValid(x, y)) return;

        ILevel level = gameState.getLevel();
        Resource boulder = new ResourceBoulder();
        boulder.getPosition().set(x * level.getTileWidth(), y * level.getTileHeight());

        gameState.getLevel().addResource(boulder);
        console.log("Spawned boulder at " + x + " " + y, LogLevel.SUCCESS);
    }

    public void spawnSoldierUnit(int x, int y) {
        if (!isPositionValid(x, y)) return;

        ILevel level = gameState.getLevel();
        SoldierUnit unit = new SoldierUnit(gameState.getLevel());
        unit.getPosition().set(x * level.getTileWidth(), y * level.getTileHeight());

        gameState.getLevel().addPlayerUnit(unit);
        console.log("Spawned unit at " + x + " " + y, LogLevel.SUCCESS);
    }

    public void spawnWorkerUnit(int x, int y) {
        if (!isPositionValid(x, y)) return;

        ILevel level = gameState.getLevel();
        WorkerUnit worker = new WorkerUnit(gameState.getLevel());
        worker.getPosition().set(x * level.getTileWidth(), y * level.getTileHeight());

        gameState.getLevel().addPlayerUnit(worker);
        console.log("Spawned worker unit at " + x + " " + y, LogLevel.SUCCESS);
    }

    public void spawnBugUnit(int x, int y) {
        if (!isPositionValid(x, y)) return;

        ILevel level = gameState.getLevel();
        AlienBugUnit alienBugUnit = new AlienBugUnit(level);
        alienBugUnit.getPosition().set(x * level.getTileWidth(), y * level.getTileHeight());

        level.addHostileUnit(alienBugUnit);
        console.log("Spawned alien bug at " + x + " " + y, LogLevel.SUCCESS);
    }

    public void addMinerals(int minerals) {
        gameState.addMinerals(minerals);
        console.log("Added " + minerals + " minerals.", LogLevel.SUCCESS);
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
