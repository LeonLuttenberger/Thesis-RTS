package hr.fer.zemris.zavrsni.rts.world.controllers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.sun.media.jfxmediaimpl.MediaDisposer.Disposable;
import hr.fer.zemris.zavrsni.rts.IUpdatable;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.projectiles.Projectile;
import hr.fer.zemris.zavrsni.rts.objects.units.Squad;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;
import hr.fer.zemris.zavrsni.rts.objects.units.hostile.HostileUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.player.PlayerUnit;
import hr.fer.zemris.zavrsni.rts.world.GameState;
import hr.fer.zemris.zavrsni.rts.world.IGameState;
import hr.fer.zemris.zavrsni.rts.world.ILevel;
import hr.fer.zemris.zavrsni.rts.world.renderers.DragBoxRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class WorldController implements Disposable, IUpdatable {

    private final IGameState gameState;
    private final InputController inputController;

    private boolean isPaused = false;
    private boolean wasPausedLastUpdate = false;

    public WorldController(ILevel level, OrthographicCamera camera, DragBoxRenderer dragBoxRenderer) {
        this.gameState = new GameState();
        this.gameState.setLevel(level);

        this.inputController = new InputController(dragBoxRenderer, camera, this);
    }

    public IGameState getGameState() {
        return gameState;
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
                level::removeResource, r -> r.onResourceDestroyed(gameState));
    }

    private void removeDestroyedBuildings() {
        ILevel level = gameState.getLevel();
        removeFromLevelIf(level.getBuildings(), IDamageable::isDestroyed,
                level::removeBuilding, null);
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

    public void sendSelectedUnitsTo(Vector3 destination) {
        Squad squad = gameState.createSquadFromSelected();

        if (squad != null) {
            squad.sendToLocation(destination.x, destination.y);
        }
    }

    @Override
    public void dispose() {
    }

    public InputProcessor getInputProcessor() {
        return inputController;
    }
}
