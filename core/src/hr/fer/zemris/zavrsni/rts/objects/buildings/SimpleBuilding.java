package hr.fer.zemris.zavrsni.rts.objects.buildings;

import hr.fer.zemris.zavrsni.rts.assets.Assets;

public class SimpleBuilding extends Building {

    public SimpleBuilding() {
        super(Assets.getInstance().getBuildings().manufactory, 224, 224);
    }

    @Override
    public void update(float deltaTime) {

    }
}
