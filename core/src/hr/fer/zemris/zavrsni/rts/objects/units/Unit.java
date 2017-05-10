package hr.fer.zemris.zavrsni.rts.objects.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import hr.fer.zemris.zavrsni.rts.objects.AbstractMovableObject;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.search.ISearchAgent;
import hr.fer.zemris.zavrsni.rts.search.impl.MapTile;
import hr.fer.zemris.zavrsni.rts.search.impl.RTAAStarMapSearchAgent;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public abstract class Unit extends AbstractMovableObject implements IDamageable {

    private static final float TOLERANCE = 4;

    private final Animation<TextureRegion> animation;
    protected final ILevel level;

    private final float defaultSpeed;
    private final int maxHealth;
    private final float attackRange;
    private final int attackPower;

    private int health;

    private boolean flipX;
    private float stateTime;

    private final ISearchAgent<MapTile> searchAgent;
    private final Vector2 goalPosition = new Vector2();
    private final Vector2 currentGoal = new Vector2();
    private MapTile goalTile;
    private MapTile waypointTile;

    public Unit(Animation<TextureRegion> animation, ILevel level, float width, float height,
                float defaultSpeed, int maxHealth, float attackRange, int attackPower) {
        this.animation = animation;
        this.level = level;
        this.searchAgent = new RTAAStarMapSearchAgent(level);

        this.dimension.x = width;
        this.dimension.y = height;

        this.defaultSpeed = defaultSpeed;
        this.maxHealth = maxHealth;
        this.attackRange = attackRange;
        this.attackPower = attackPower;

        this.health = maxHealth;
    }

    public final float getDefaultSpeed() {
        return defaultSpeed;
    }

    public float getAttackRange() {
        return attackRange;
    }

    public int getAttackPower() {
        return attackPower;
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
                //TODO
//                flipX = false;
                flipX = true;
            }
        }

        batch.draw(frame.getTexture(), position.x, position.y, origin.x, origin.y,
                dimension.x, dimension.y, scale.x, scale.y, rotation,
                frame.getRegionX(), frame.getRegionY(), frame.getRegionWidth(),
                frame.getRegionHeight(), flipX, false);
    }

    @Override
    public void update(float deltaTime) {

        // make sure velocity doesn't pass the max
        float maxSpeed = level.getTerrainModifier(getCenterX(), getCenterY()) * defaultSpeed;
        if (velocity.len() > maxSpeed) {
            velocity.setLength(maxSpeed);
        }

        // check if unit collides with inpassable object
        float newX = getCenterX() + velocity.x * deltaTime;
        float newY = getCenterY() + velocity.y * deltaTime;

        int newTileX = (int) (newX / level.getTileWidth());
        int newTileY = (int) (newY / level.getTileHeight());
        if (level.getTileModifier(newTileX, newTileY) <= 0) {
            searchAgent.stopSearch();
            goalTile = null;
            velocity.setLength(0);
        }

        super.update(deltaTime);
    }

    public void updateSearchAgent() {
        if (goalTile != null) {
            if (isOnTile(goalTile)) {
                currentGoal.set(goalPosition);
                if (distance(getCenterX(), getCenterY(), goalPosition.x, goalPosition.y) > TOLERANCE) {
                    velocity.x = goalPosition.x - getCenterX();
                    velocity.y = goalPosition.y - getCenterY();
                    velocity.setLength(level.getTerrainModifier(getCenterX(), getCenterY()) * defaultSpeed);
                } else {
                    searchAgent.stopSearch();
                    goalTile = null;
                    velocity.setLength(0);
                }
            } else {
                if (waypointTile != null && isOnTile(waypointTile)) {
                    waypointTile = searchAgent.update(waypointTile);
                }

                MapTile nextTile;
                if (waypointTile == null) {
                    nextTile = goalTile;
                } else {
                    nextTile = waypointTile;
                }

                float currentGoalX = (nextTile.x * level.getTileWidth() + level.getTileWidth() / 2f);
                float currentGoalY = (nextTile.y * level.getTileHeight() + level.getTileHeight() / 2f);
                currentGoal.set(currentGoalX, currentGoalY);

                float dx = currentGoalX - getCenterX();
                float dy = currentGoalY - getCenterY();

                velocity.set(dx, dy);
                velocity.setLength(level.getTerrainModifier(getCenterX(), getCenterY()) * defaultSpeed);
            }
        }
    }

    private boolean isOnTile(MapTile tile) {
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

    private MapTile getMapTile(float x, float y) {
        return new MapTile(
                (int) (x / level.getTileWidth()),
                (int) (y / level.getTileHeight())
        );
    }

    private MapTile getCurrentMapTile() {
        return getMapTile(getCenterX(), getCenterY());
    }

    public void sendToDestination(float x, float y) {
        goalPosition.set(x, y);
        goalTile = getMapTile(x, y);

        searchAgent.pathfind(getCurrentMapTile(), goalTile);
        waypointTile = searchAgent.update(getCurrentMapTile());
    }

    public boolean isSearchStopped() {
        return goalTile == null;
    }

    public void stopSearch() {
        searchAgent.stopSearch();
        goalTile = null;
        velocity.setLength(0);
    }

    public Vector2 getCurrentGoal() {
        return currentGoal;
    }

    public abstract boolean isHostile();

    public abstract boolean isSupport();

    @Override
    public int getMaxHitPoints() {
        return maxHealth;
    }

    @Override
    public int getCurrentHitPoints() {
        return health;
    }

    @Override
    public void addHitPoints(int repair) {
        health += repair;
    }

    @Override
    public void removeHitPoints(int damage) {
        health += damage;
    }
}
