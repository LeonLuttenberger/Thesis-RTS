public class StickmanSpawner extends HostileBuilding {
    private static final int WIDTH = 96;
    private static final int HEIGHT = 64;
    private static final int MAX_HIT_POINTS = 300;

    private int currentWork = 0;
    private IBuildableUnit currentUnit = new StickmanUnit(level);

    public StickmanSpawner(ILevel level) {
        super(level, WIDTH, HEIGHT, MAX_HIT_POINTS);
    }

    @Override 
    public TextureRegion loadTexture() {
        return Assets.getInstance().getBuildings().stickmanSpawner;
    }

    @Override 
    public void update(float deltaTime) {
        super.update(deltaTime);

        updateCurrentWork();
        if (currentWork >= currentUnit.getTrainingCost()) {
            spawnUnit(currentUnit);

            resetCurrentWork();
            currentUnit = new StickmanUnit(level);
        }
    }
}