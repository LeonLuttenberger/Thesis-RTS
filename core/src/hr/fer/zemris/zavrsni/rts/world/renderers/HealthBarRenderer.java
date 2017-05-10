package hr.fer.zemris.zavrsni.rts.world.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import hr.fer.zemris.zavrsni.rts.assets.AssetHealthBar;
import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.objects.units.player.PlayerUnit;
import hr.fer.zemris.zavrsni.rts.world.IGameState;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public class HealthBarRenderer {

    private static final int HEALTH_BAR_HEIGHT = 5;

    private final IGameState gameState;
    private final AssetHealthBar healthBarAssets;
    private final SpriteBatch batch;

    public HealthBarRenderer(IGameState gameState) {
        this.gameState = gameState;
        this.healthBarAssets = Assets.getInstance().getHealthBar();

        this.batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(
                0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()
        );
    }

    public void render(OrthographicCamera camera) {
        ILevel level = gameState.getLevel();

        batch.begin();

        for (PlayerUnit playerUnit : level.getPlayerUnits()) {
            drawHealthBar(camera, playerUnit, playerUnit.getPosition(), playerUnit.getDimension());
        }

        batch.end();
    }

    private final Vector3 leftPointAboveObject = new Vector3();
    private final Vector3 rightPointAboveObject = new Vector3();

    private void drawHealthBar(OrthographicCamera camera, IDamageable damageable, Vector2 position, Vector2 dimension) {
        int hitPoints = damageable.getCurrentHitPoints();
        int maxHitPoints = damageable.getMaxHitPoints();
        int divisions = healthBarAssets.healthBars.size();

        int healthBarIndex = Math.round((divisions  - 1) * hitPoints / (float) maxHitPoints);
        AtlasRegion atlasRegion = healthBarAssets.healthBars.get(healthBarIndex);

        leftPointAboveObject.set(position.x, position.y + dimension.y, 0);
        rightPointAboveObject.set(position.x + dimension.x, position.y + dimension.y, 0);

        Vector3 start = camera.project(leftPointAboveObject);
        Vector3 end = camera.project(rightPointAboveObject);

        batch.draw(atlasRegion, start.x, start.y, end.x - start.x, HEALTH_BAR_HEIGHT);
    }
}
