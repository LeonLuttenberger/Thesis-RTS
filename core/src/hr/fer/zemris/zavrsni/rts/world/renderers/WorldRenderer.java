package hr.fer.zemris.zavrsni.rts.world.renderers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import hr.fer.zemris.zavrsni.rts.common.IGameSettings;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.units.HostileUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.PlayerUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;
import hr.fer.zemris.zavrsni.rts.pathfinding.tiled.map.MapTile;
import hr.fer.zemris.zavrsni.rts.world.controllers.RTSWorldController;

public class WorldRenderer extends OrthogonalTiledMapRenderer {

    private final RTSWorldController controller;
    private final SpriteBatch spriteBatch;
    private final IGameSettings gameSettings;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final BitmapFont font = new BitmapFont();

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
        controller.getGameState().getLevel().render(spriteBatch);
        controller.getControllerState().render(spriteBatch);

//        renderMapModifiers();
        spriteBatch.end();

        renderUnitSelection();

        if (gameSettings.showPathFindingRoutes()) {
            renderPathFindingIndicators();
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

    private void renderPathFindingIndicators() {
        ILevel level = controller.getGameState().getLevel();

        Matrix4 oldProjMatrix = shapeRenderer.getProjectionMatrix();
        shapeRenderer.setProjectionMatrix(spriteBatch.getProjectionMatrix());
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

    private void renderMapModifiers() {
        ILevel level = controller.getGameState().getLevel();

        for (int i = 0, xEnd = level.getWidth(); i < xEnd; i++) {
            int x = i * level.getTileWidth();

            for (int j = 0, yEnd = level.getHeight(); j < yEnd; j++) {
                int y = j * level.getTileHeight();

                float tileModifier = level.getTileModifier(i, j);
                String string = String.format("%.1f", tileModifier);

                float lineHeight = font.getLineHeight();
                font.draw(spriteBatch, string, x + 5, y + level.getTileHeight() / 2 + lineHeight / 2);
            }
        }
    }
}
