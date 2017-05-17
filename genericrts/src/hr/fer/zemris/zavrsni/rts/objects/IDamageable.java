package hr.fer.zemris.zavrsni.rts.objects;

import hr.fer.zemris.zavrsni.rts.common.IGameState;

public interface IDamageable<T extends AbstractGameObject> {

    int getMaxHitPoints();

    int getCurrentHitPoints();

    void addHitPoints(int repair);

    void removeHitPoints(int damage);

    void onDestroyed(IGameState gameState);

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
