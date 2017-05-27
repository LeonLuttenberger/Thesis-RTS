package hr.fer.zemris.zavrsni.rts.objects;

import hr.fer.zemris.zavrsni.rts.objects.projectiles.Projectile;

import java.io.Serializable;

public interface IRangedAttacker<T extends AbstractGameObject> extends Serializable {

    Projectile rangedAttack(IDamageable<? extends AbstractGameObject> target);

    float getAttackRange();

    default T getAttacker() {
        if (this instanceof AbstractGameObject) {
            return (T) this;
        }

        throw new ClassCastException("IRangedAttacker is not an AbstractGameObject.");
    }
}
