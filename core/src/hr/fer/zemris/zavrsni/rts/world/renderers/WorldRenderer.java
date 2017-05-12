package hr.fer.zemris.zavrsni.rts.world.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.world.controllers.WorldController;

public class WorldRenderer extends OrthogonalTiledMapRenderer {

    private WorldController controller;
    private SpriteBatch spriteBatch;

    private Building ghostNewBuilding;

    public WorldRenderer(WorldController controller, TiledMap tiledMap, SpriteBatch spriteBatch) {
        super(tiledMap, spriteBatch);

        this.controller = controller;
        this.spriteBatch = spriteBatch;
    }

    public void setGhostNewBuilding(Building ghostNewBuilding) {
        this.ghostNewBuilding = ghostNewBuilding;
    }

    @Override
    public void render() {
        super.render();

        spriteBatch.begin();

        controller.getGameState().getLevel().render(spriteBatch);

        if (ghostNewBuilding != null) {
            Color oldColor = spriteBatch.getColor();
            spriteBatch.setColor(1, 1, 1, 0.5f);
            ghostNewBuilding.render(spriteBatch);
            spriteBatch.setColor(oldColor);
        }

        spriteBatch.end();
    }
}
