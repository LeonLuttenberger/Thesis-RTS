package hr.fer.zemris.zavrsni.rts.objects;

import hr.fer.zemris.zavrsni.rts.objects.projectiles.Projectile;

public interface IRangedAttacker<T extends AbstractGameObject> {

    Projectile rangedAttack(IDamageable<? extends AbstractGameObject> target);

    float getAttackRange();

    default T getAttacker() {
        if (this instanceof AbstractGameObject) {
            return (T) this;
        }

        throw new ClassCastException("IRangedAttacker is not an AbstractGameObject.");
    }
}
