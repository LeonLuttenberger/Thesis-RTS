package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.InputProcessor;
import com.sun.media.jfxmediaimpl.MediaDisposer.Disposable;
import hr.fer.zemris.zavrsni.rts.common.IGameState;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.common.IUpdateable;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.projectiles.Projectile;
import hr.fer.zemris.zavrsni.rts.objects.units.HostileUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.Squad;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class AbstractRTSWorldController implements IUpdateable, Disposable {

    protected final IGameState gameState;
    private IControllerState controllerState;

    private boolean isPaused = false;
    private boolean wasPausedLastUpdate = false;

    protected AbstractRTSWorldController(IGameState gameState, IControllerState controllerState) {
        this.gameState = Objects.requireNonNull(gameState);
        this.controllerState = Objects.requireNonNull(controllerState);
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    @Override
    public void update(float deltaTime) {
        if (isPaused) {
            wasPausedLastUpdate = true;
            return;
        }

        deltaTime = wasPausedLastUpdate ? 0 : deltaTime;
        wasPausedLastUpdate = false;

        handleInput(deltaTime);

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
        removeUsedUpProjectiles();
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

    private void removeUsedUpProjectiles() {
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

    @Override
    public void dispose() {

    }

    public abstract InputProcessor getInputProcessor();

    protected abstract void handleInput(float deltaTime);

    public IGameState getGameState() {
        return gameState;
    }

    public IControllerState getControllerState() {
        return controllerState;
    }

    public void setControllerState(IControllerState controllerState) {
        this.controllerState = Objects.requireNonNull(controllerState);
    }
}
