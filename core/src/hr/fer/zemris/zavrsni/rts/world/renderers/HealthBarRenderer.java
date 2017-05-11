package hr.fer.zemris.zavrsni.rts.world.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import hr.fer.zemris.zavrsni.rts.assets.AssetHealthBar;
import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.objects.IDamageTrackable;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.resources.Resource;
import hr.fer.zemris.zavrsni.rts.objects.units.hostile.HostileUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.player.PlayerUnit;
import hr.fer.zemris.zavrsni.rts.world.IGameState;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public class HealthBarRenderer {

    private static final int HEALTH_BAR_HEIGHT = 5;
    private static final float HIT_EFFECT_DURATION = 0.05f;

    private final IGameState gameState;
    private final AssetHealthBar healthBarAssets;
    private final SpriteBatch batch;

    private boolean isEnabledForPlayerUnits = true;
    private boolean isEnabledForHostileUnits = true;
    private boolean isEnabledForBuildings = true;
    private boolean isEnabledForResources = false;

    public HealthBarRenderer(IGameState gameState) {
        this.gameState = gameState;
        this.healthBarAssets = Assets.getInstance().getHealthBar();

        this.batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(
                0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()
        );
    }

    public void setEnabledForPlayerUnits(boolean enabledForPlayerUnits) {
        isEnabledForPlayerUnits = enabledForPlayerUnits;
    }

    public void setEnabledForHostileUnits(boolean enabledForHostileUnits) {
        isEnabledForHostileUnits = enabledForHostileUnits;
    }

    public void setEnabledForBuildings(boolean enabledForBuildings) {
        isEnabledForBuildings = enabledForBuildings;
    }

    public void setEnabledForResources(boolean enabledForResources) {
        isEnabledForResources = enabledForResources;
    }

    public void render(OrthographicCamera camera) {
        ILevel level = gameState.getLevel();

        batch.begin();

        if (isEnabledForPlayerUnits) {
            for (PlayerUnit playerUnit : level.getPlayerUnits()) {
                drawHealthBar(camera, playerUnit, playerUnit.getPosition(), playerUnit.getDimension());
            }
        }

        if (isEnabledForHostileUnits) {
            for (HostileUnit hostileUnit : level.getHostileUnits()) {
                drawHealthBar(camera, hostileUnit, hostileUnit.getPosition(), hostileUnit.getDimension());
            }
        }

        if (isEnabledForBuildings) {
            for (Building building : level.getBuildings()) {
                drawHealthBar(camera, building, building.getPosition(), building.getDimension());
            }
        }

        if (isEnabledForResources) {
            for (Resource resource : level.getResources()) {
                drawHealthBar(camera, resource, resource.getPosition(), resource.getDimension());
            }
        }

        batch.end();
    }

    private final Vector3 leftPointAboveObject = new Vector3();
    private final Vector3 rightPointAboveObject = new Vector3();

    private void drawHealthBar(OrthographicCamera camera, IDamageable damageable, Vector2 position, Vector2 dimension) {
        int hitPoints = damageable.getCurrentHitPoints();
        int maxHitPoints = damageable.getMaxHitPoints();
        int divisions = healthBarAssets.healthBars.size();

        AtlasRegion atlasRegion;
        if (!(damageable instanceof IDamageTrackable) ||
                ((IDamageTrackable) damageable).timeSinceDamageLastTaken() > HIT_EFFECT_DURATION) {

            int healthBarIndex = Math.round((divisions  - 1) * hitPoints / (float) maxHitPoints);
            atlasRegion = healthBarAssets.healthBars.get(healthBarIndex);
        } else {
            atlasRegion = healthBarAssets.healthBarHit;
        }

        leftPointAboveObject.set(position.x, position.y + dimension.y, 0);
        rightPointAboveObject.set(position.x + dimension.x, position.y + dimension.y, 0);

        Vector3 start = camera.project(leftPointAboveObject);
        Vector3 end = camera.project(rightPointAboveObject);

        batch.draw(atlasRegion, start.x, start.y, end.x - start.x, HEALTH_BAR_HEIGHT);
    }
}
