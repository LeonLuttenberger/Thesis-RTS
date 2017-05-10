package hr.fer.zemris.zavrsni.rts.objects;

public interface IDamageable {

    int getMaxHitPoints();

    int getCurrentHitPoints();

    void addHitPoints(int repair);

    void removeHitPoints(int damage);

    default boolean isDestroyed() {
        return getCurrentHitPoints() == 0;
    }
}
