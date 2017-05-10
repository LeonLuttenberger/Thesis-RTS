package hr.fer.zemris.zavrsni.rts.world.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import hr.fer.zemris.zavrsni.rts.objects.resources.Resource;
import hr.fer.zemris.zavrsni.rts.objects.units.Squad;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;
import hr.fer.zemris.zavrsni.rts.world.GameState;
import hr.fer.zemris.zavrsni.rts.world.IGameState;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

import java.util.ArrayList;
import java.util.List;

public class WorldController implements IWorldController {

    private final Game game;
    private final IGameState gameState;

    private boolean isPaused = false;
    private boolean wasPausedLastUpdate = false;

    public WorldController(Game game, ILevel level) {
        this.game = game;

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

        List<Squad> squadsToRemove = new ArrayList<>();
        for (Squad squad : gameState.getSquads()) {
            squad.update(deltaTime);
            if (squad.isSearchStopped()) {
                squadsToRemove.add(squad);
            }
        }
        for (Squad squad : squadsToRemove) {
            gameState.removeSquad(squad);
        }

        ILevel level = gameState.getLevel();
        for (Unit unit : level.getPlayerUnits()) {
            unit.update(deltaTime);
        }

        List<Resource> resourcesToRemove = new ArrayList<>();
        for (Resource resource : level.getResources()) {
            if (resource.getRemainingDurability() <= 0) {
                resource.onResourceDestroyed(gameState);
                resourcesToRemove.add(resource);
            }
        }
        for (Resource resource : resourcesToRemove) {
            level.removeResource(resource);
        }
    }

    public void selectUnitsInArea(Vector3 areaStart, Vector3 areaEnd) {
        Rectangle selectionArea = new Rectangle(
                Math.min(areaStart.x, areaEnd.x),
                Math.min(areaStart.y, areaEnd.y),
                Math.abs(areaEnd.x - areaStart.x),
                Math.abs(areaEnd.y - areaStart.y)
        );

        for (Unit unit : gameState.getLevel().getPlayerUnits()) {
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
