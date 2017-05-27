package hr.fer.zemris.zavrsni.rts.world.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import hr.fer.zemris.zavrsni.rts.common.IGameSettings;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.common.map.MapTile;
import hr.fer.zemris.zavrsni.rts.objects.units.HostileUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.PlayerUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;
import hr.fer.zemris.zavrsni.rts.world.controllers.RTSWorldController;

public class WorldRenderer extends OrthogonalTiledMapRenderer {

    private final RTSWorldController controller;
    private final SpriteBatch spriteBatch;
    private final IGameSettings gameSettings;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public WorldRenderer(RTSWorldController controller, TiledMap tiledMap, SpriteBatch spriteBatch,
                         IGameSettings gameSettings) {
        super(tiledMap, spriteBatch);

        this.controller = controller;
        this.spriteBatch = spriteBatch;
        this.gameSettings = gameSettings;
    }

    @Override
    public void render() {
        super.render();

        spriteBatch.begin();
        controller.getControllerState().render(spriteBatch);
        controller.getGameState().getLevel().render(spriteBatch);
        spriteBatch.end();

        renderUnitSelection();

        if (gameSettings.showPathFindingRoutes()) {
            renderPathFindingIndicators(spriteBatch);
        }
    }

    private void renderUnitSelection() {
        shapeRenderer.setProjectionMatrix(spriteBatch.getProjectionMatrix());
        shapeRenderer.begin(ShapeType.Line);

        for (PlayerUnit playerUnit : controller.getGameState().getLevel().getPlayerUnits()) {
            if (playerUnit.isSelected()) {
                shapeRenderer.ellipse(
                        playerUnit.getCenterX() - playerUnit.dimension.x / 2,
                        playerUnit.getCenterY() - playerUnit.dimension.y / 2,
                        playerUnit.dimension.x, playerUnit.dimension.y);
            }
        }

        shapeRenderer.end();
    }

    private void renderPathFindingIndicators(SpriteBatch batch) {
        ILevel level = controller.getGameState().getLevel();

        Matrix4 oldProjMatrix = shapeRenderer.getProjectionMatrix();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeType.Line);

        for (PlayerUnit unit : level.getPlayerUnits()) {
            renderPathFindingIndicator(level, unit);
        }
        for (HostileUnit unit : level.getHostileUnits()) {
            renderPathFindingIndicator(level, unit);
        }

        shapeRenderer.end();
        shapeRenderer.setProjectionMatrix(oldProjMatrix);
    }

    private void renderPathFindingIndicator(ILevel level, Unit unit) {
        float previousX = unit.getCenterX();
        float previousY = unit.getCenterY();

        for (MapTile mapTile : unit.getStatesQueue()) {
            float x = level.getTileWidth() * mapTile.x + level.getTileWidth() / 2;
            float y = level.getTileHeight() * mapTile.y + level.getTileHeight() / 2;

            shapeRenderer.line(previousX, previousY, x, y);
            previousX = x;
            previousY = y;
        }
    }
}
