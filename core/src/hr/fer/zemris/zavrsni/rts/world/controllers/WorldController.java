package hr.fer.zemris.zavrsni.rts.world.controllers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import hr.fer.zemris.zavrsni.rts.common.GameState;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.common.IUpdateable;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.objects.buildings.BaseBuilding;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.projectiles.Projectile;
import hr.fer.zemris.zavrsni.rts.objects.units.HostileUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.IBuildableUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.PlayerUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.Squad;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;
import hr.fer.zemris.zavrsni.rts.world.controllers.state.DefaultControllerState;
import hr.fer.zemris.zavrsni.rts.world.controllers.state.IControllerState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class WorldController implements Disposable, IUpdateable {

    private final GameState gameState;
    private final InputController inputController;

    private final OrthographicCamera camera;

    private boolean isPaused = false;
    private boolean wasPausedLastUpdate = false;

    private IControllerState controllerState;
    private BaseBuilding baseBuilding;

    public WorldController(ILevel level, OrthographicCamera camera) {
        this.gameState = new GameState();
        this.gameState.setLevel(level);

        this.camera = camera;
        this.inputController = new InputController(camera, this);
        this.controllerState = new DefaultControllerState(this);

        baseBuilding = level.getBuildings().stream()
                .filter(b -> b instanceof BaseBuilding)
                .map(b -> (BaseBuilding) b)
                .findFirst().orElseThrow(() -> new RuntimeException("Level does not contain a base."));
    }

    public GameState getGameState() {
        return gameState;
    }

    public IControllerState getControllerState() {
        return controllerState;
    }

    public void setControllerState(IControllerState controllerState) {
        this.controllerState = Objects.requireNonNull(controllerState);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    public void update(float deltaTime) {
        if (isPaused) {
            wasPausedLastUpdate = true;
            return;
        }

        deltaTime = wasPausedLastUpdate ? 0 : deltaTime;
        wasPausedLastUpdate = false;

        inputController.handleInput(deltaTime);

        ILevel level = gameState.getLevel();

        for (Squad squad : gameState.getSquads()) {
            squad.update(deltaTime);
        }

        for (Unit unit : level.getPlayerUnits()) {
            unit.update(deltaTime);
        }
        for (HostileUnit unit : level.getHostileUnits()) {
            unit.update(deltaTime);
        }
        for (Building building : level.getBuildings()) {
            building.update(deltaTime);
        }
        for (Projectile projectile : level.getProjectiles()) {
            projectile.update(deltaTime);
        }

        removeDeadUnits();
        removeUnnecessarySquads();
        removeCollectedResources();
        removeDestroyedBuildings();
        removeUsedUpProjectile();
    }

    private void removeDeadUnits() {
        ILevel level = gameState.getLevel();
        removeFromLevelIf(level.getPlayerUnits(), IDamageable::isDestroyed, level::removePlayerUnit, null);
        removeFromLevelIf(level.getHostileUnits(), IDamageable::isDestroyed, level::removeHostileUnit, null);
    }

    private void removeUnnecessarySquads() {
        removeFromLevelIf(gameState.getSquads(), Squad::isSearchStopped,
                gameState::removeSquad, null);
    }

    private void removeCollectedResources() {
        ILevel level = gameState.getLevel();
        removeFromLevelIf(level.getResources(), IDamageable::isDestroyed,
                level::removeResource, r -> r.onDestroyed(gameState));
    }

    private void removeDestroyedBuildings() {
        ILevel level = gameState.getLevel();
        removeFromLevelIf(level.getBuildings(), IDamageable::isDestroyed,
                level::removeBuilding, b -> b.onDestroyed(gameState));
    }

    private void removeUsedUpProjectile() {
        ILevel level = gameState.getLevel();
        removeFromLevelIf(level.getProjectiles(), Projectile::isUsedUp, level::removeProjectile, null);
    }

    private static <T> void removeFromLevelIf(List<T> objects, Predicate<T> condition,
                                       Consumer<T> removeFunction, Consumer<T> removalEvent) {

        List<T> toRemove = new ArrayList<>();
        for (T object : objects) {
            if (condition.test(object)) {
                toRemove.add(object);
            }
        }

        for (T t : toRemove) {
            removeFunction.accept(t);
            if (removalEvent != null) {
                removalEvent.accept(t);
            }
        }
    }

    public void selectUnitsInArea(Vector3 areaStart, Vector3 areaEnd) {
        Rectangle selectionArea = new Rectangle(
                Math.min(areaStart.x, areaEnd.x),
                Math.min(areaStart.y, areaEnd.y),
                Math.abs(areaEnd.x - areaStart.x),
                Math.abs(areaEnd.y - areaStart.y)
        );

        for (PlayerUnit unit : gameState.getLevel().getPlayerUnits()) {
            if (selectionArea.contains(unit.getCenterX(), unit.getCenterY())) {
                unit.setSelected(true);
            } else {
                unit.setSelected(false);
            }
        }
    }

    public void deselectUnits() {
        for (PlayerUnit playerUnit : gameState.getLevel().getPlayerUnits()) {
            playerUnit.setSelected(false);
        }
    }

    public void sendSelectedUnitsTo(Vector3 destination) {
        Squad squad = gameState.createSquadFromSelected();

        if (squad != null) {
            squad.sendToLocation(destination.x, destination.y);
        }
    }

    public InputProcessor getInputProcessor() {
        return inputController;
    }

    public void buildUnit(Function<ILevel, IBuildableUnit> function) {
        baseBuilding.buildUnit(function);
    }

    @Override
    public void dispose() {
    }
}
