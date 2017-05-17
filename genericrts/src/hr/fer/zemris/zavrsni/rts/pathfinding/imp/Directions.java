package hr.fer.zemris.zavrsni.rts.pathfinding.imp;

public enum Directions {

    NORTH (new Transition(0, 1)),
    SOUTH (new Transition(0, -1)),
    EAST (new Transition(1, 0)),
    WEST (new Transition(-1, 0)),

    NORTH_EAST (new Transition(1, 1)),
    NORTH_WEST (new Transition(-1, 1)),
    SOUTH_EAST (new Transition(1, -1)),
    SOUTH_WEST (new Transition(-1, -1));

    private final Transition direction;

    Directions(Transition direction) {
        this.direction = direction;
    }

    public Transition getDirection() {
        return direction;
    }
}
