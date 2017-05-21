package hr.fer.zemris.zavrsni.rts.world.controllers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import hr.fer.zemris.zavrsni.rts.common.IGameState;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.buildings.BaseBuilding;
import hr.fer.zemris.zavrsni.rts.objects.units.IBuildableUnit;
import hr.fer.zemris.zavrsni.rts.world.AbstractRTSWorldController;
import hr.fer.zemris.zavrsni.rts.world.controllers.state.DefaultControllerState;

import java.util.function.Function;

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
