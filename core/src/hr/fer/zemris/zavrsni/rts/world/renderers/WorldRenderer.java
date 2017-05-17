package hr.fer.zemris.zavrsni.rts.world.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import hr.fer.zemris.zavrsni.rts.common.GameState;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.buildings.BuildingCosts;
import hr.fer.zemris.zavrsni.rts.world.controllers.WorldController;

public class WorldRenderer extends OrthogonalTiledMapRenderer {

    private WorldController controller;
    private SpriteBatch spriteBatch;

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

        if (controller.getGhostBuilding() != null) {
            Color oldColor = spriteBatch.getColor();

            if (isCostSatisfied()) {
                spriteBatch.setColor(1, 1, 1, 0.5f);
            } else {
                spriteBatch.setColor(1, 0, 0, 0.5f);
            }

            controller.getGhostBuilding().render(spriteBatch);
            spriteBatch.setColor(oldColor);
        }

        spriteBatch.end();
    }

    private boolean isCostSatisfied() {
        GameState gameState = controller.getGameState();
        Building ghostBuilding = controller.getGhostBuilding();

        return BuildingCosts.getCostFor(ghostBuilding.getClass()).isSatisfied(gameState);
    }
}
