package hr.fer.zemris.zavrsni.rts.objects;

public interface IDamageTrackable<T extends AbstractGameObject> extends IDamageable<T> {

    float timeSinceDamageLastTaken();
}
