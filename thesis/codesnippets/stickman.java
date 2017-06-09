public class StickmanUnit extends HostileUnit implements IBuildableUnit {
    private static final int UNIT_WIDTH = 32;
    private static final int UNIT_HEIGHT = 32;
    private static final float SPEED = 200;
    private static final int MAX_HEALTH = 100;
    private static final int ATTACK_RANGE = 50;
    private static final int ATTACK_POWER = 5;
    private static final float ATTACK_COOLDOWN = 0.3f;
    private static final float DETECTION_RANGE = 150f;

    public StickmanUnit(ILevel level) {
        super(level, 
             UNIT_WIDTH, UNIT_HEIGHT, SPEED, MAX_HEALTH, 
             ATTACK_RANGE, ATTACK_POWER, ATTACK_COOLDOWN, DETECTION_RANGE
        );
    }

    @Override
    public Animation<TextureRegion> loadAnimation() {
        return Assets.getInstance().getUnits().stickmanAnimation;
    }

    @Override
    public boolean isSupport() {
        return false;
    }

    @Override
    public int getTrainingCost() {
        return 500;
    }
}
