package hr.fer.zemris.zavrsni.rts.objects.buildings;

import hr.fer.zemris.zavrsni.rts.common.ILevel;

public abstract class PlayerBuilding extends Building {

    private static final long serialVersionUID = -5416206696699072719L;

    public PlayerBuilding(ILevel level, float width, float height, int maxHitPoints) {
        super(level, width, height, maxHitPoints);
    }
}
