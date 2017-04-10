package hr.fer.zemris.zavrsni.rts.world;

public class GameState {

    private Level level;

    public GameState() {
        reset();
    }

    private void reset() {

    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
