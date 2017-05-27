package hr.fer.zemris.zavrsni.rts.objects.buildings;

import hr.fer.zemris.zavrsni.rts.common.ILevel;

public abstract class HostileBuilding extends Building {

    private static final long serialVersionUID = -7277910659234557373L;

    public HostileBuilding(ILevel level, float width, float height, int maxHitPoints) {
        super(level, width, height, maxHitPoints);
    }
}
