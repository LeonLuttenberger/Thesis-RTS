package hr.fer.zemris.zavrsni.rts.world.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector3;
import hr.fer.zemris.zavrsni.rts.assets.AssetHealthBar;
import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.common.IGameSettings;
import hr.fer.zemris.zavrsni.rts.common.IGameState;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.IDamageTrackable;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.resources.Resource;
import hr.fer.zemris.zavrsni.rts.objects.units.HostileUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.PlayerUnit;

public class HealthBarRenderer {

    private static final int HEALTH_BAR_HEIGHT = 5;
    private static final float HIT_EFFECT_DURATION = 0.05f;

    private final IGameState gameState;
    private final AssetHealthBar healthBarAssets;
    private final SpriteBatch batch;

    private final IGameSettings gameSettings;

    public HealthBarRenderer(IGameState gameState, IGameSettings gameSettings) {
        this.gameState = gameState;
        this.gameSettings = gameSettings;
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

        if (gameSettings.showPlayerUnitHealthBars()) {
            for (PlayerUnit playerUnit : level.getPlayerUnits()) {
                drawHealthBar(camera, playerUnit);
            }
        }

        if (gameSettings.showHostileUnitHealthBars()) {
            for (HostileUnit hostileUnit : level.getHostileUnits()) {
                drawHealthBar(camera, hostileUnit);
            }
        }

        if (gameSettings.showBuildingHealthBars()) {
            for (Building building : level.getBuildings()) {
                drawHealthBar(camera, building);
            }
        }

        if (gameSettings.showResourceHealthBars()) {
            for (Resource resource : level.getResources()) {
                drawHealthBar(camera, resource);
            }
        }

        batch.end();
    }

    public void resize(int width, int height) {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
    }

    private final Vector3 leftPointAboveObject = new Vector3();
    private final Vector3 rightPointAboveObject = new Vector3();

    private void drawHealthBar(OrthographicCamera camera, IDamageable<? extends AbstractGameObject> damageable) {
        AbstractGameObject object = damageable.getObject();

        int hitPoints = damageable.getCurrentHitPoints();
        int maxHitPoints = damageable.getMaxHitPoints();
        int divisions = healthBarAssets.healthBars.size();

        AtlasRegion atlasRegion;
        if (!(damageable instanceof IDamageTrackable) ||
                ((IDamageTrackable<?>) damageable).timeSinceDamageLastTaken() > HIT_EFFECT_DURATION) {

            int healthBarIndex = Math.round((divisions  - 1) * hitPoints / (float) maxHitPoints);
            atlasRegion = healthBarAssets.healthBars.get(healthBarIndex);
        } else {
            atlasRegion = healthBarAssets.healthBarHit;
        }

        leftPointAboveObject.set(object.position.x, object.position.y + object.dimension.y, 0);
        rightPointAboveObject.set(object.position.x + object.dimension.x, object.position.y + object.dimension.y, 0);

        Vector3 start = camera.project(leftPointAboveObject);
        Vector3 end = camera.project(rightPointAboveObject);

        batch.draw(atlasRegion, start.x, start.y, end.x - start.x, HEALTH_BAR_HEIGHT);
    }
}
