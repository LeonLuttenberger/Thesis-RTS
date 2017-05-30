package hr.fer.zemris.zavrsni.rts.objects.buildings;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.units.IBuildableUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.PlayerUnit;
import hr.fer.zemris.zavrsni.rts.pathfinding.tiled.map.MapTile;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Function;

public class BaseBuilding extends PlayerBuilding {

    private static final long serialVersionUID = 4726698633216314701L;

    private static final int WIDTH = 224;
    private static final int HEIGHT = 224;

    private static final int MAX_HIT_POINTS = 1000;
    private static final int TRAINING_INCREMENT = 5;
    private Queue<TrainingJob> trainingJobs = new ArrayDeque<>();

    public BaseBuilding(ILevel level) {
        super(level, WIDTH, HEIGHT, MAX_HIT_POINTS);
    }

    @Override
    public TextureRegion loadTexture() {
        return Assets.getInstance().getBuildings().manufactory;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (!trainingJobs.isEmpty()) {
            TrainingJob currentJob = trainingJobs.peek();

            currentJob.workDone += TRAINING_INCREMENT;
            if (currentJob.workDone >= currentJob.unit.getTrainingCost()) {
                trainingJobs.poll();

                spawnUnit(currentJob.unit);
            }
        }
    }

    private void spawnUnit(IBuildableUnit buildableUnit) {
        if (!(buildableUnit instanceof PlayerUnit)) {
            throw new UnsupportedOperationException("Base only build player units.");
        }

        MapTile spawnPosition = level.getTileForPosition(position.x - 1, position.y + dimension.y / 2);

        PlayerUnit unit = (PlayerUnit) buildableUnit;
        unit.position.x = level.getTileWidth() * spawnPosition.x;
        unit.position.y = level.getTileHeight() * spawnPosition.y;

        level.addPlayerUnit(unit);
    }

    public void buildUnit(Function<ILevel, IBuildableUnit> unitFunction) {
        trainingJobs.add(new TrainingJob(unitFunction.apply(level)));
    }

    private static class TrainingJob {
        IBuildableUnit unit;
        int workDone = 0;

        TrainingJob(IBuildableUnit unit) {
            this.unit = unit;
        }
    }
}
