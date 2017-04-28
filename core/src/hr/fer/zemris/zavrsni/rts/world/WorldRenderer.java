package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;

public class WorldRenderer extends IsometricTiledMapRenderer {

    private IWorldController controller;
    private SpriteBatch spriteBatch;

    public WorldRenderer(IWorldController controller, TiledMap tiledMap, SpriteBatch spriteBatch) {
        super(tiledMap, spriteBatch);

        this.controller = controller;
        this.spriteBatch = spriteBatch;
    }

    @Override
    public void render() {
        super.render();

        spriteBatch.begin();
        controller.getGameState().getLevel().render(spriteBatch);
        spriteBatch.end();
    }
}
