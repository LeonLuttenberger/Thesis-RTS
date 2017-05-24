package hr.fer.zemris.zavrsni.rts.world.controllers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import hr.fer.zemris.zavrsni.rts.common.IGameState;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.buildings.BaseBuilding;
import hr.fer.zemris.zavrsni.rts.objects.units.HostileUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.IBuildableUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.Unit;
import hr.fer.zemris.zavrsni.rts.objects.units.squad.Squad;
import hr.fer.zemris.zavrsni.rts.world.AbstractRTSWorldController;
import hr.fer.zemris.zavrsni.rts.world.controllers.state.DefaultControllerState;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RTSWorldController extends AbstractRTSWorldController {

    private final InputController inputController;
    private final OrthographicCamera camera;

    private BaseBuilding baseBuilding;

    public RTSWorldController(IGameState gameState, OrthographicCamera camera) {
        super(gameState, new DefaultControllerState(camera, gameState));

        this.camera = camera;
        this.inputController = new InputController(camera, this);

        baseBuilding = gameState.getLevel().getBuildings().stream()
                .filter(b -> b instanceof BaseBuilding)
                .map(b -> (BaseBuilding) b)
                .findFirst().orElseThrow(() -> new RuntimeException("Level does not contain a base."));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        ILevel level = gameState.getLevel();
        Set<HostileUnit> unitsInSquads = gameState.getSquads().stream()
                .flatMap(s -> s.getSquadMembers().stream())
                .filter(u -> u instanceof HostileUnit)
                .map(u -> (HostileUnit) u)
                .collect(Collectors.toSet());

        Optional<Squad> squadOptional = createEnemySquadIfPossible(level.getHostileUnits(), unitsInSquads, 3);
        if (squadOptional.isPresent()) {
            Squad squad = squadOptional.get();
            squad.sendToLocation(baseBuilding.position.x, baseBuilding.position.y);
            gameState.addSquad(squad);
        }
    }

    private Optional<Squad> createEnemySquadIfPossible(List<HostileUnit> units, Set<HostileUnit> unitsAlreadyInSquad,
                                                       int minSize) {

        Set<Unit> squadMembers = new HashSet<>();

        for (HostileUnit unit : units) {
            if (unitsAlreadyInSquad.contains(unit)) continue;
            squadMembers.clear();
            squadMembers.add(unit);

            for (HostileUnit secondUnit : units) {
                if (unit == secondUnit) continue;
                if (unitsAlreadyInSquad.contains(unit)) continue;

                if (Unit.distanceBetween(unit, secondUnit) < unit.getAttackRange()) {
                    squadMembers.add(secondUnit);
                }
            }

            if (squadMembers.size() >= minSize) {
                return Optional.of(new Squad(squadMembers, gameState.getLevel()));
            }
        }

        return Optional.empty();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    @Override
    public InputProcessor getInputProcessor() {
        return inputController;
    }

    @Override
    protected void handleInput(float deltaTime) {
        inputController.handleInput(deltaTime);
    }

    public void buildUnit(Function<ILevel, IBuildableUnit> function) {
        baseBuilding.buildUnit(function);
    }
}
