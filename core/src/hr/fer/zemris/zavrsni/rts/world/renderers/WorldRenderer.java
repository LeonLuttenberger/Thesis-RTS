package hr.fer.zemris.zavrsni.rts.world.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import hr.fer.zemris.zavrsni.rts.objects.units.PlayerUnit;
import hr.fer.zemris.zavrsni.rts.world.controllers.WorldController;

public class WorldRenderer extends OrthogonalTiledMapRenderer {

    private WorldController controller;
    private SpriteBatch spriteBatch;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public WorldRenderer(WorldController controller, TiledMap tiledMap, SpriteBatch spriteBatch) {
        super(tiledMap, spriteBatch);

        this.controller = controller;
        this.spriteBatch = spriteBatch;
    }

    @Override
    public void render() {
        super.render();

        spriteBatch.begin();

        controller.getGameState().getLevel().render(spriteBatch);
        controller.getControllerState().render(spriteBatch);

        spriteBatch.end();

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
}
