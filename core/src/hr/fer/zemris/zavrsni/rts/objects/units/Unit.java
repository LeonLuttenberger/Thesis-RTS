package hr.fer.zemris.zavrsni.rts.objects.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import hr.fer.zemris.zavrsni.rts.objects.AbstractMovableObject;
import hr.fer.zemris.zavrsni.rts.search.SearchNode;
import hr.fer.zemris.zavrsni.rts.search.SearchResult;
import hr.fer.zemris.zavrsni.rts.search.algorithms.AStarSearch;
import hr.fer.zemris.zavrsni.rts.search.algorithms.AbstractSearchAlgorithm;
import hr.fer.zemris.zavrsni.rts.search.heuristic.CachingProblemHeuristic;
import hr.fer.zemris.zavrsni.rts.search.heuristic.IHeuristic;
import hr.fer.zemris.zavrsni.rts.search.heuristic.WeightedHeuristic;
import hr.fer.zemris.zavrsni.rts.search.impl.ArealDistanceHeuristic;
import hr.fer.zemris.zavrsni.rts.search.impl.MapPathFindingProblem;
import hr.fer.zemris.zavrsni.rts.search.impl.MapPosition;
import hr.fer.zemris.zavrsni.rts.search.problem.RTAAStarProblem;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

public abstract class Unit extends AbstractMovableObject {

    private static final String TAG = Unit.class.getName();

    private static final IHeuristic<MapPosition> HEURISTIC = new CachingProblemHeuristic<>(new WeightedHeuristic<>(
            new ArealDistanceHeuristic(), 2));
    private static final AbstractSearchAlgorithm<MapPosition> A_STAR_SEARCH = new AStarSearch<>();

    private static final float TOLERANCE = 10;
    private static final int MAX_STATES_TO_EXPAND = 100;
    private static final int MAX_MOVES = 20;

    protected final Animation<TextureRegion> animation;
    protected final ILevel level;
    protected final float defaultSpeed;

    private boolean flipX;
    protected boolean isSelected;
    private float stateTime;

    private final Vector2 goalPosition = new Vector2();
    private SearchNode<MapPosition> currentState;
    private MapPosition goalState;
    private boolean hasDestination;
    private RTAAStarProblem<MapPosition, MapPathFindingProblem> searchProblem;
    private SearchResult<MapPosition> searchResult;
    private int movesMade;

    public Unit(Animation<TextureRegion> animation, ILevel level, float width, float height, float defaultSpeed) {
        this.animation = animation;
        this.level = level;

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

    private void resetSearch() {
        if (searchResult != null) {
            double currentStateCost = currentState.getCost();
            double currentStateHeuristic = currentState.getHeuristic();

            for (SearchNode<MapPosition> searchNode : searchResult.getClosedSet()) {
                double f = currentStateCost + currentStateHeuristic;
                double correctedHeuristic = f - searchNode.getCost();
                searchProblem.cacheHeuristic(searchNode.getState(), correctedHeuristic);
            }
        }

        searchProblem.setNewStartState(getMapPosition(getCenterX(), getCenterY()));
        searchResult = A_STAR_SEARCH.search(searchProblem, HEURISTIC);
        movesMade = 0;
    }

    private void stopSearch() {
        hasDestination = false;
        searchResult = null;
        searchProblem = null;
        velocity.setLength(0);
    }

    @Override
    public void update(float deltaTime) {
        if (hasDestination) {
            if (isOnTile(goalState)) {
                if (distance(getCenterX(), getCenterY(), goalPosition.x, goalPosition.y) > TOLERANCE) {
                    velocity.x = goalPosition.x - getCenterX();
                    velocity.y = goalPosition.y - getCenterY();
                    velocity.setLength(level.getTerrainModifier(getCenterX(), getCenterY()) * defaultSpeed);
                    super.update(deltaTime);
                } else {
                    stopSearch();
                }
                return;
            }

            if (searchResult == null || searchResult.getStatesQueue().isEmpty()) {
                resetSearch();

                if (searchResult == null) {
                    Gdx.app.log(TAG, "Could not find path to destination.");
                    stopSearch();
                    return;
                }

                currentState = searchResult.getStatesQueue().peek();
                Gdx.app.log(TAG, "Moving towards " + currentState.getState());
            }

            // check for changes
            for (SearchNode<MapPosition> stateOnPath : searchResult.getStatesQueue()) {
                MapPosition positionOnPath = stateOnPath.getState();

                float cachedModifier = searchProblem.getProblem().getProblem().getCachedModifier(positionOnPath);
                float currentModifier = level.getTileModifier(positionOnPath.x, positionOnPath.y);
                if (cachedModifier != currentModifier) {
                    searchResult.getStatesQueue().clear();
                    return;
                }
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
        currentState = searchResult.getStatesQueue().peek();
        MapPosition nextPosition = currentState.getState();
        float speed = level.getTerrainModifier(getCenterX(), getCenterY()) * defaultSpeed;

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
        goalState = getMapPosition(x, y);
        hasDestination = true;
        searchResult = null;

        searchProblem = new RTAAStarProblem<>(
                new MapPathFindingProblem(getMapPosition(getCenterX(), getCenterY()), goalState, level),
                MAX_STATES_TO_EXPAND
        );

        Gdx.app.log(TAG, "Set goal to: " + goalState);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
