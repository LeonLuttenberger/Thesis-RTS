package hr.fer.zemris.zavrsni.rts.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Level {

    public static final String TAG = Level.class.getName();

    private Texture texture;
    final Sprite[][] sprites = new Sprite[10][10];

    public Level() {
        texture = new Texture(Gdx.files.internal("images/grass.png"));

        for(int z = 0; z < 10; z++) {
            for(int x = 0; x < 10; x++) {
                sprites[x][z] = new Sprite(texture);
                sprites[x][z].setPosition(x,z);
                sprites[x][z].setSize(1, 1);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for(int z = 0; z < 10; z++) {
            for(int x = 0; x < 10; x++) {
                sprites[x][z].draw(batch);
            }
        }
    }
}
