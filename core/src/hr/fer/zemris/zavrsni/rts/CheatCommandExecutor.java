package hr.fer.zemris.zavrsni.rts;

import com.strongjoshua.console.CommandExecutor;
import com.strongjoshua.console.Console;
import hr.fer.zemris.zavrsni.rts.objects.resources.ResourceBoulder;
import hr.fer.zemris.zavrsni.rts.util.Constants;
import hr.fer.zemris.zavrsni.rts.world.IGameState;

public class CheatCommandExecutor extends CommandExecutor {

    private final IGameState gameState;

    public CheatCommandExecutor(IGameState gameState) {
        this.gameState = gameState;
    }

    @Override
    protected void setConsole(Console c) {
        super.setConsole(c);
    }

    public void greet(String name) {
        console.log("Hello " + name);
    }

    public void addBoulder(int x, int y) {
        ResourceBoulder boulder = new ResourceBoulder();
        boulder.getPosition().set(x * Constants.TILE_WIDTH, y * Constants.TILE_HEIGHT);

        gameState.getLevel().addResource(boulder);
    }

    public void clear() {
        console.clear();
    }
}
