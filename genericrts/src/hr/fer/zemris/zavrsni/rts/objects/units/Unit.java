package hr.fer.zemris.zavrsni.rts.objects.units;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import hr.fer.zemris.zavrsni.rts.common.IGameState;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.IDamageTrackable;
import hr.fer.zemris.zavrsni.rts.pathfinding.ISearchAgent;
import hr.fer.zemris.zavrsni.rts.pathfinding.tiled.SearchAgentProvider;
import hr.fer.zemris.zavrsni.rts.pathfinding.tiled.map.MapTile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;

public abstract class Unit extends AbstractGameObject implements IDamageTrackable<Unit> {

    private static final long serialVersionUID = 9093619222088384779L;

    private static final float TOLERANCE = 4;

    private transient Animation<TextureRegion> animation;
    protected final ILevel level;

    protected final float defaultSpeed;
    protected final int maxHealth;
    protected final float attackRange;
    protected final int attackPower;
    protected final float attackCooldown;

    protected int health;
    protected float timeSinceLastAttack = 0;
    protected float timeSinceLastDamageTaken = 0;

    private boolean flipX;
    private float stateTime;

    private transient ISearchAgent<MapTile> searchAgent;
    private final Vector2 goalPosition = new Vector2();
    private final Vector2 currentGoal = new Vector2();
    protected final Vector2 newVelocity = new Vector2();
    private MapTile goalTile;
    private MapTile waypointTile;

    public Unit(ILevel level, float width, float height,
                float defaultSpeed, int maxHealth, float attackRange, int attackPower, float attackCooldown) {
        this.animation = loadAnimation();
        this.level = level;
        this.searchAgent = SearchAgentProvider.getSearchAgent(level);

        this.dimension.x = width;
        this.dimension.y = height;

        this.defaultSpeed = defaultSpeed;
        this.maxHealth = maxHealth;
        this.attackRange = attackRange;
        this.attackPower = attackPower;
        this.attackCooldown = attackCooldown;

        this.health = maxHealth;
    }

    public abstract Animation<TextureRegion> loadAnimation();

    public float getAttackRange() {
        return attackRange;
    }

    @Override
    public void render(SpriteBatch batch) {
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
        stateTime += deltaTime;
        velocity.set(newVelocity);

        timeSinceLastAttack += deltaTime;
        timeSinceLastDamageTaken += deltaTime;
        updateMovements(deltaTime);

        super.update(deltaTime);
        newVelocity.setLength(0);
    }

    private void updateMovements(float deltaTime) {
        MapTile goalTile = searchAgent.getGoalState();
        if (!isSearchStopped() && level.getTileModifier(goalTile.x, goalTile.y) == 0) {
            if (searchAgent.isGoalState(level.getTileForPosition(getCenterX(), getCenterY()))) {
                stopSearch();
                return;
            }
        }

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
            stopSearch();
            velocity.setLength(0);
        }
    }

    public void updateSearchAgent() {
        if (goalTile != null) {
            if (isOnTile(goalTile)) {
                currentGoal.set(goalPosition);
                if (distance(getCenterX(), getCenterY(), goalPosition.x, goalPosition.y) > TOLERANCE) {
                    newVelocity.x = goalPosition.x - getCenterX();
                    newVelocity.y = goalPosition.y - getCenterY();
                    newVelocity.setLength(level.getTerrainModifier(getCenterX(), getCenterY()) * defaultSpeed);
                } else {
                    searchAgent.stopSearch();
                    goalTile = null;
                    newVelocity.setLength(0);
                }
            } else {
                if (waypointTile != null && isOnTile(waypointTile)) {
                    waypointTile = searchAgent.getAction(waypointTile);
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

                newVelocity.set(dx, dy);
                newVelocity.setLength(level.getTerrainModifier(getCenterX(), getCenterY()) * defaultSpeed);
            }
        }
    }

    public void adjustDirection(float x, float y) {
        newVelocity.add(x, y);
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

    private MapTile getCurrentMapTile() {
        return level.getTileForPosition(getCenterX(), getCenterY());
    }

    public void sendToDestination(float x, float y) {
        goalPosition.set(x, y);
        goalTile = level.getTileForPosition(x, y);

        searchAgent.pathfind(getCurrentMapTile(), goalTile);
        waypointTile = searchAgent.getAction(getCurrentMapTile());
    }

    public boolean isSearchStopped() {
        return goalTile == null;
    }

    public void stopSearch() {
        searchAgent.stopSearch();
        goalTile = null;
        velocity.setLength(0);
    }

    public Iterable<MapTile> getStatesQueue() {
        if (searchAgent == null) {
            return Collections.emptyList();
        }

        return searchAgent.getStatesQueue();
    }

    public Vector2 getGoalPosition() {
        return goalPosition;
    }

    public Vector2 getCurrentGoal() {
        return currentGoal;
    }

    public abstract boolean isSupport();

    protected boolean readyForAttack() {
        return timeSinceLastAttack > attackCooldown;
    }

    protected final void resetAttackCooldown() {
        timeSinceLastAttack = 0;
    }

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
        health = Math.max(health + repair, maxHealth);
    }

    @Override
    public void removeHitPoints(int damage) {
        health = Math.max(health - damage, 0);
        timeSinceLastDamageTaken = 0;
    }

    @Override
    public float timeSinceDamageLastTaken() {
        return timeSinceLastDamageTaken;
    }

    @Override
    public void onDestroyed(IGameState gameState) {
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        searchAgent = SearchAgentProvider.getSearchAgent(level);
        this.animation = loadAnimation();
    }
}
