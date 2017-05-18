package hr.fer.zemris.zavrsni.rts.world.controllers.state;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import hr.fer.zemris.zavrsni.rts.common.IGameState;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.common.MapTile;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.buildings.BuildingCosts;
import hr.fer.zemris.zavrsni.rts.objects.buildings.BuildingCosts.Cost;

import java.util.function.Function;

public class BuildingControllerState implements IControllerState {

    private final Function<ILevel, Building> buildingConstructor;
    private final OrthographicCamera camera;
    private final IGameState gameState;

    private Building template;

    public BuildingControllerState(Function<ILevel, Building> buildingConstructor,
                                   OrthographicCamera camera, IGameState gameState) {
        this.buildingConstructor = buildingConstructor;
        this.camera = camera;
        this.gameState = gameState;

        this.template = buildingConstructor.apply(gameState.getLevel());
    }

    @Override
    public void mouseMoved(int screenX, int screenY) {
        Vector3 position = camera.unproject(new Vector3(screenX, screenY, 0));

        ILevel level = gameState.getLevel();
        MapTile mapTile = level.getTileForPosition(position.x, position.y);

        template.setCenterX(mapTile.x * level.getTileWidth() + level.getTileWidth() / 2f);
        template.setCenterY(mapTile.y * level.getTileHeight() + level.getTileHeight() / 2f);
    }

    @Override
    public void mouseClicked(int screenX, int screenY) {
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
    public void render(SpriteBatch batch) {
        Color oldColor = batch.getColor();

        if (BuildingCosts.getCostFor(template.getClass()).isSatisfied(gameState)) {
            batch.setColor(1, 1, 1, 0.5f);
        } else {
            batch.setColor(1, 0, 0, 0.5f);
        }

        template.render(batch);
        batch.setColor(oldColor);
    }
}
