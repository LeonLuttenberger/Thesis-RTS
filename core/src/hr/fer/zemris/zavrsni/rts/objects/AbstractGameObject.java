package hr.fer.zemris.zavrsni.rts.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public abstract class AbstractGameObject {

    private final Body body;

    public AbstractGameObject(World world, Vector2 position, BodyType bodyType) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(position);

        this.body = world.createBody(bodyDef);
    }

    public Body getBody() {
        return body;
    }
}
