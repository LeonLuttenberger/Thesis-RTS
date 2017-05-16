package hr.fer.zemris.zavrsni.rts.objects;

public interface IDamageable<T extends AbstractGameObject> {

    int getMaxHitPoints();

    int getCurrentHitPoints();

    void addHitPoints(int repair);

    void removeHitPoints(int damage);

    default boolean isDestroyed() {
        return getCurrentHitPoints() == 0;
    }

    default T getObject() {
        if (this instanceof AbstractGameObject) {
            return (T) this;
        }

        throw new ClassCastException("IDamageable is not an AbstractGameObject.");
    }
}
