package hr.fer.zemris.zavrsni.rts.world.controllers.state;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import hr.fer.zemris.zavrsni.rts.common.IGameState;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.common.costs.BuildingCosts;
import hr.fer.zemris.zavrsni.rts.common.costs.Cost;
import hr.fer.zemris.zavrsni.rts.common.map.MapTile;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.world.ControllerStateAdapter;
import hr.fer.zemris.zavrsni.rts.world.controllers.RTSWorldController;

import java.util.function.Function;

public class BuildingControllerState extends ControllerStateAdapter {

    private final Function<ILevel, Building> buildingConstructor;
    private final RTSWorldController controller;

    private Building template;

    public BuildingControllerState(Function<ILevel, Building> buildingConstructor, RTSWorldController controller) {
        this.buildingConstructor = buildingConstructor;
        this.controller = controller;

        this.template = buildingConstructor.apply(controller.getGameState().getLevel());
    }

    @Override
    public void mouseMoved(int screenX, int screenY) {
        Vector3 position = controller.getCamera().unproject(new Vector3(screenX, screenY, 0));

        ILevel level = controller.getGameState().getLevel();
        MapTile mapTile = level.getTileForPosition(position.x, position.y);

        template.setCenterX(mapTile.x * level.getTileWidth() + level.getTileWidth() / 2f);
        template.setCenterY(mapTile.y * level.getTileHeight() + level.getTileHeight() / 2f);
    }

    @Override
    public void mouseLeftClicked(int screenX, int screenY) {
        IGameState gameState = controller.getGameState();

        Cost cost = BuildingCosts.getCostFor(template.getClass());
        if (!cost.isSatisfied(gameState)) return;

        Building building = buildingConstructor.apply(gameState.getLevel());
        building.position.set(template.position);

        boolean isAdded = gameState.getLevel().addBuilding(building);
        if (isAdded) {
            cost.apply(gameState);
        }
    }

    @Override
    public void mouseRightClicked(int screenX, int screenY) {
        controller.setControllerState(new DefaultControllerState(controller.getCamera(), controller.getGameState()));
    }

    @Override
    public void render(SpriteBatch batch) {
        Color oldColor = batch.getColor();

        if (BuildingCosts.getCostFor(template.getClass()).isSatisfied(controller.getGameState())) {
            batch.setColor(1, 1, 1, 0.5f);
        } else {
            batch.setColor(1, 0, 0, 0.5f);
        }

        template.render(batch);
        batch.setColor(oldColor);
    }
}
