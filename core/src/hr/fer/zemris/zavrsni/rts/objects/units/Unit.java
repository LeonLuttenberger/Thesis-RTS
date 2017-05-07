package hr.fer.zemris.zavrsni.rts.objects.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import hr.fer.zemris.zavrsni.rts.objects.AbstractMovableObject;
import hr.fer.zemris.zavrsni.rts.search.ISearchAgent;
import hr.fer.zemris.zavrsni.rts.search.impl.MapPosition;
import hr.fer.zemris.zavrsni.rts.search.impl.RTAAStarMapSearchAgent;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public abstract class Unit extends AbstractMovableObject {

    private static final float TOLERANCE = 4;

    private final Animation<TextureRegion> animation;
    protected final ILevel level;
    private final float defaultSpeed;

    private boolean flipX;
    private boolean isSelected;
    private float stateTime;

    private final ISearchAgent<MapPosition> searchAgent;
    private final Vector2 goalPosition = new Vector2();
    private MapPosition goalState;
    private MapPosition waypointTile;

    public Unit(Animation<TextureRegion> animation, ILevel level, float width, float height, float defaultSpeed) {
        this.animation = animation;
        this.level = level;
        this.searchAgent = new RTAAStarMapSearchAgent(level);

        this.dimension.x = width;
        this.dimension.y = height;

        this.defaultSpeed = defaultSpeed;
    }

    public float getDefaultSpeed() {
        return defaultSpeed;
    }

    @Override
    public void render(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion frame;
        if (!isMoving()) {
            frame = animation.getKeyFrame(0);
        } else {
            frame = animation.getKeyFrame(stateTime);
            if (velocity.x < 0) {
                flipX = true;
            } else if (velocity.x > 0) {
                flipX = false;
            }
        }

        batch.draw(frame.getTexture(), position.x, position.y, origin.x, origin.y,
                dimension.x, dimension.y, scale.x, scale.y, rotation,
                frame.getRegionX(), frame.getRegionY(), frame.getRegionWidth(),
                frame.getRegionHeight(), flipX, false);
    }

    @Override
    public void update(float deltaTime) {
        if (goalState != null) {
            if (isOnTile(goalState)) {
                if (distance(getCenterX(), getCenterY(), goalPosition.x, goalPosition.y) > TOLERANCE) {
                    velocity.x = goalPosition.x - getCenterX();
                    velocity.y = goalPosition.y - getCenterY();
                    velocity.setLength(level.getTerrainModifier(getCenterX(), getCenterY()) * defaultSpeed);
                } else {
                    searchAgent.stopSearch();
                    goalState = null;
                    velocity.setLength(0);
                }
            } else {
                if (isOnTile(waypointTile)) {
                    waypointTile = searchAgent.update(waypointTile);
                }

                float currentGoalX = (waypointTile.x * level.getTileWidth() + level.getTileWidth() / 2f);
                float currentGoalY = (waypointTile.y * level.getTileHeight() + level.getTileHeight() / 2f);

                float dx = currentGoalX - getCenterX();
                float dy = currentGoalY - getCenterY();

                velocity.set(dx, dy);
                velocity.setLength(level.getTerrainModifier(getCenterX(), getCenterY()) * defaultSpeed);
            }
        }

        super.update(deltaTime);
    }

    private boolean isOnTile(MapPosition tile) {
        float centerX = getCenterX();
        float centerY = getCenterY();
        float tilePosX = tile.x * level.getTileWidth();
        float tilePosY = tile.y * level.getTileHeight();

        if (centerX < tilePosX) return false;
        if (centerX >= tilePosX + level.getTileWidth()) return false;
        if (centerY < tilePosY) return false;
        if (centerY >= tilePosY + level.getTileHeight()) return false;

        return true;
    }

    private static float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private MapPosition getMapPosition(float x, float y) {
        return new MapPosition(
                (int) (x / level.getTileWidth()),
                (int) (y / level.getTileHeight())
        );
    }

    private MapPosition getCurrentMapPosition() {
        return getMapPosition(getCenterX(), getCenterY());
    }

    public void goToLocation(float x, float y) {
        goalPosition.set(x, y);
        goalState = getMapPosition(x, y);

        searchAgent.pathfind(getCurrentMapPosition(), goalState);
        waypointTile = searchAgent.update(getCurrentMapPosition());
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
