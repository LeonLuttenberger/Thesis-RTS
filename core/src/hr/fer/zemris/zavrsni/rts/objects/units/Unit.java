package hr.fer.zemris.zavrsni.rts.objects.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import hr.fer.zemris.zavrsni.rts.objects.AbstractMovableObject;
import hr.fer.zemris.zavrsni.rts.objects.IDamageTrackable;
import hr.fer.zemris.zavrsni.rts.pathfinding.ISearchAgent;
import hr.fer.zemris.zavrsni.rts.pathfinding.impl.MapTile;
import hr.fer.zemris.zavrsni.rts.pathfinding.impl.RTAAStarMapSearchAgent;
import hr.fer.zemris.zavrsni.rts.util.LevelUtils;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public abstract class Unit extends AbstractMovableObject implements IDamageTrackable<Unit> {

    private static final float TOLERANCE = 4;

    private final Animation<TextureRegion> animation;
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

    private ISearchAgent<MapTile> searchAgent;
    private final Vector2 goalPosition = new Vector2();
    private final Vector2 currentGoal = new Vector2();
    protected final Vector2 newVelocity = new Vector2();
    private MapTile goalTile;
    private MapTile waypointTile;

    public Unit(Animation<TextureRegion> animation, ILevel level, float width, float height,
                float defaultSpeed, int maxHealth, float attackRange, int attackPower, float attackCooldown) {
        this.animation = animation;
        this.level = level;
        this.searchAgent = new RTAAStarMapSearchAgent(level);

        this.dimension.x = width;
        this.dimension.y = height;

        this.defaultSpeed = defaultSpeed;
        this.maxHealth = maxHealth;
        this.attackRange = attackRange;
        this.attackPower = attackPower;
        this.attackCooldown = attackCooldown;

        this.health = maxHealth;
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
            if (searchAgent.isGoalState(getMapTile(getCenterX(), getCenterY()))) {
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

    private MapTile getMapTile(float x, float y) {
        return LevelUtils.getMapTile(level, x, y);
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

    public void transferPathfindingTo(Unit other) {
        other.goalPosition.set(this.goalPosition);
        other.currentGoal.set(this.currentGoal);
        other.goalTile = this.goalTile;

        other.searchAgent = this.searchAgent;
        other.waypointTile = this.waypointTile;
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

    public boolean readyForAttack() {
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
}
