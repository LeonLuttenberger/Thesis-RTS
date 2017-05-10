package hr.fer.zemris.zavrsni.rts.objects.buildings;

import hr.fer.zemris.zavrsni.rts.assets.Assets;

public class SimpleBuilding extends Building {

    private static final int WIDTH = 224;
    private static final int HEIGHT = 224;
    private static final int MAX_HIT_POINTS = 1000;

    public SimpleBuilding() {
        super(Assets.getInstance().getBuildings().manufactory, WIDTH, HEIGHT, MAX_HIT_POINTS);
    }

    @Override
    public void update(float deltaTime) {

    }
}
