package hr.fer.zemris.zavrsni.rts.world.controllers;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.objects.buildings.Building;
import hr.fer.zemris.zavrsni.rts.objects.units.Squad;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;
import hr.fer.zemris.zavrsni.rts.objects.units.hostile.HostileUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.player.PlayerUnit;
import hr.fer.zemris.zavrsni.rts.world.GameState;
import hr.fer.zemris.zavrsni.rts.world.IGameState;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class WorldController implements IWorldController {

    private final IGameState gameState;

    private boolean isPaused = false;
    private boolean wasPausedLastUpdate = false;

    public WorldController(ILevel level) {

        this.gameState = new GameState();
        this.gameState.setLevel(level);
    }

    public IGameState getGameState() {
        return gameState;
    }

    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
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

        for (Squad squad : gameState.getSquads()) {
            squad.update(deltaTime);
        }

        ILevel level = gameState.getLevel();
        for (Unit unit : level.getPlayerUnits()) {
            unit.update(deltaTime);
        }
        for (HostileUnit unit : level.getHostileUnits()) {
            unit.update(deltaTime);
        }
        for (Building building : level.getBuildings()) {
            building.update(deltaTime);
        }

        removeDeadUnits();
        removeUnnecessarySquads();
        removeCollectedResources();
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
        removeFromLevelIf(level.getResources(), r -> r.getCurrentHitPoints() <= 0,
                level::removeResource, r -> r.onResourceDestroyed(gameState));
    }

    private static <T> void removeFromLevelIf(List<T> objects, Predicate<T> condition,
                                       Consumer<T> removeFunction, Consumer<T> removalEvent) {

        List<T> toRemove = new ArrayList<>();
        for (T object : objects) {
            if (condition.test(object)) {
                if (removalEvent != null) {
                    removalEvent.accept(object);
                }
                toRemove.add(object);
            }
        }

        for (T t : toRemove) {
            removeFunction.accept(t);
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

    @Override
    public void sendSelectedUnitsTo(Vector3 destination) {
        Squad squad = gameState.createSquadFromSelected();

        if (squad != null) {
            squad.sendToLocation(destination.x, destination.y);
        }
    }

    @Override
    public void dispose() {
    }
}
