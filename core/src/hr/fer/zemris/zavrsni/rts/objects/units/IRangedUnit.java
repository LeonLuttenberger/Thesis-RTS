package hr.fer.zemris.zavrsni.rts.objects.units;

import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.projectiles.Projectile;

public interface IRangedUnit {

    Projectile rangedAttack(AbstractGameObject target);
}
