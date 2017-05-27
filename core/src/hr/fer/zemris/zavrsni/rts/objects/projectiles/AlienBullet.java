package hr.fer.zemris.zavrsni.rts.objects.projectiles;

import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.objects.IRangedAttacker;

public class AlienBullet extends AbstractBullet {

    private static final long serialVersionUID = -1622259040498958946L;

    public AlienBullet(ILevel level, IRangedAttacker<? extends AbstractGameObject> source,
                       IDamageable<? extends AbstractGameObject> target, int attackPower) {

        super(Assets.getInstance().getOthers().alienBullet, level, source, target, attackPower);
    }
}
