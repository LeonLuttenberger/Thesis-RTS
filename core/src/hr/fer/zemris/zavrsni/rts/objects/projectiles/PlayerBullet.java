package hr.fer.zemris.zavrsni.rts.objects.projectiles;

import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.common.ILevel;
import hr.fer.zemris.zavrsni.rts.objects.AbstractGameObject;
import hr.fer.zemris.zavrsni.rts.objects.IDamageable;
import hr.fer.zemris.zavrsni.rts.objects.IRangedAttacker;

public class PlayerBullet extends AbstractBullet {

    private static final long serialVersionUID = -3512121614325593610L;

    public PlayerBullet(ILevel level, IRangedAttacker<? extends AbstractGameObject> source,
                        IDamageable<? extends AbstractGameObject> target, int attackPower) {

        super(Assets.getInstance().getOthers().bullet, level, source, target, attackPower);
    }
}
