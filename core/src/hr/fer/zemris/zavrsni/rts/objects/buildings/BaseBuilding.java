package hr.fer.zemris.zavrsni.rts.objects.buildings;

import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.objects.units.IBuildableUnit;
import hr.fer.zemris.zavrsni.rts.objects.units.player.PlayerUnit;
import hr.fer.zemris.zavrsni.rts.pathfinding.impl.MapTile;
import hr.fer.zemris.zavrsni.rts.util.LevelUtils;
import hr.fer.zemris.zavrsni.rts.world.ILevel;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Function;

public class BaseBuilding extends Building {

    private static final int WIDTH = 224;
    private static final int HEIGHT = 224;
    private static final int MAX_HIT_POINTS = 1000;

    private static final int TRAINING_INCREMENT = 1;

    private Queue<TrainingJob> trainingJobs = new ArrayDeque<>();

    public BaseBuilding(ILevel level) {
        super(Assets.getInstance().getBuildings().manufactory, level, WIDTH, HEIGHT, MAX_HIT_POINTS);
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

        MapTile spawnPosition = LevelUtils.getMapTile(level, position.x - 1, position.y + dimension.y / 2);

        PlayerUnit unit = (PlayerUnit) buildableUnit;
        unit.getPosition().x = level.getTileWidth() * spawnPosition.x;
        unit.getPosition().y = level.getTileHeight() * spawnPosition.y;

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
