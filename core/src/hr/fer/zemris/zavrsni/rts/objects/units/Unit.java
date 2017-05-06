package hr.fer.zemris.zavrsni.rts.objects.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import hr.fer.zemris.zavrsni.rts.objects.AbstractMovableObject;
import hr.fer.zemris.zavrsni.rts.search.LimitedExpansionProblem;
import hr.fer.zemris.zavrsni.rts.search.SearchResult;
import hr.fer.zemris.zavrsni.rts.search.algorithms.AStarSearch;
import hr.fer.zemris.zavrsni.rts.search.algorithms.AbstractSearchAlgorithm;
import hr.fer.zemris.zavrsni.rts.search.impl.ArealDistanceHeuristic;
import hr.fer.zemris.zavrsni.rts.search.impl.MapPathFindingProblem;
import hr.fer.zemris.zavrsni.rts.search.impl.MapPosition;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public abstract class Unit extends AbstractMovableObject {

    private static final String TAG = Unit.class.getName();

    private static final AbstractSearchAlgorithm<MapPosition> A_STAR_SEARCH =
            new AStarSearch<>(new ArealDistanceHeuristic());

    private static final float TOLERANCE = 10;
    private static final int MAX_STATES_TO_EXPAND = 200;
    private static final int MAX_MOVES = 10;

    protected final Animation<TextureRegion> animation;
    protected final ILevel level;
    private boolean flipX;

    protected boolean isSelected;
    private float stateTime;

    private final Vector2 goalPosition = new Vector2();
    private MapPosition goalTile;
    private boolean hasDestination;
    private LimitedExpansionProblem<MapPosition> searchProblem;
    private SearchResult<MapPosition> searchResult;
    private int movesMade;

    public Unit(Animation<TextureRegion> animation, ILevel level, float width, float height) {
        this.animation = animation;
        this.level = level;

        this.dimension.x = width;
        this.dimension.y = height;
    }

    @Override
    public abstract float getMaxSpeed();

    @Override
    public void render(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();

        if (!isMoving()) {
            TextureRegion frame = animation.getKeyFrame(0);
            batch.draw(frame.getTexture(), position.x, position.y, origin.x,
                    origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
                    frame.getRegionX(), frame.getRegionY(), frame.getRegionWidth(),
                    frame.getRegionHeight(), false, false);
            return;
        }

        TextureRegion frame = animation.getKeyFrame(stateTime);
        if (velocity.x < 0) {
            flipX = true;
        } else if (velocity.x > 0) {
            flipX = false;
        }

        batch.draw(frame.getTexture(), position.x, position.y, origin.x, origin.y,
                dimension.x, dimension.y, scale.x, scale.y, rotation,
                frame.getRegionX(), frame.getRegionY(), frame.getRegionWidth(),
                frame.getRegionHeight(), flipX, false);
    }

    @Override
    public void update(float deltaTime) {
        if (hasDestination) {
            if (isOnTile(goalTile)) {
                if (distance(getCenterX(), getCenterY(), goalPosition.x, goalPosition.y) > TOLERANCE) {
                    velocity.x = goalPosition.x - getCenterX();
                    velocity.y = goalPosition.y - getCenterY();
                    velocity.setLength(level.getTerrainModifier(getCenterX(), getCenterY()) * getMaxSpeed());
                    super.update(deltaTime);
                } else {
                    velocity.setLength(0);
                    searchResult = null;
                    searchProblem = null;
                    hasDestination = false;
                }
                return;
            }

            if (searchResult == null || searchResult.getStatesQueue().isEmpty()) {
                searchProblem = new LimitedExpansionProblem<>(new MapPathFindingProblem(
                    getMapPosition(getCenterX(), getCenterY()), goalTile, level
                ), MAX_STATES_TO_EXPAND);
                searchResult = A_STAR_SEARCH.search(searchProblem);
                movesMade = 0;

                if (searchResult == null) {
                    hasDestination = false;
                    searchResult = null;
                    searchProblem = null;
                    velocity.setLength(0);
                    return;
                }

                MapPosition peek = searchResult.getFrontierQueue().peek().getState();
                Gdx.app.log(TAG, "Moving towards " + peek);
            }

            boolean nextPositionReached = moveToNextPosition(deltaTime);
            if (nextPositionReached) {
                movesMade++;
            }

            if (movesMade >= MAX_MOVES) {
                searchResult.getStatesQueue().clear();
                return;
            }
        }
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

    private boolean moveToNextPosition(float deltaTime) {
        MapPosition nextPosition = searchResult.getStatesQueue().peek();
        float speed = level.getTerrainModifier(position.x, position.y) * getMaxSpeed();

        float currentGoalX = (nextPosition.x * level.getTileWidth() + level.getTileWidth() / 2f);
        float currentGoalY = (nextPosition.y * level.getTileHeight() + level.getTileHeight() / 2f);

        float dx = currentGoalX - getCenterX();
        float dy = currentGoalY - getCenterY();

        velocity.set(dx, dy);
        velocity.setLength(speed);

        super.update(deltaTime);

        if (distance(getCenterX(), getCenterY(), currentGoalX, currentGoalY) < TOLERANCE) {
            searchResult.getStatesQueue().poll();
            return true;
        }

        return false;
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

    public void goToLocation(float x, float y) {
        goalPosition.set(x, y);
        goalTile = getMapPosition(x, y);
        hasDestination = true;
        Gdx.app.log(TAG, "Set goal to: " + goalTile);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
