package hr.fer.zemris.zavrsni.rts.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class DragBoxRenderer {

    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private Rectangle rectangle = new Rectangle();

    private boolean drawing = false;

    public DragBoxRenderer() {
        shapeRenderer.getProjectionMatrix().setToOrtho2D(
                0, Gdx.graphics.getHeight(),
                Gdx.graphics.getWidth(), -Gdx.graphics.getHeight()
        );
    }

    public void handleTouchDrag(int x, int y) {
        if (!drawing) {
            rectangle.x = x;
            rectangle.y = y;
            drawing = true;
        }

        rectangle.width = x - rectangle.x;
        rectangle.height = y - rectangle.y;
    }

    public void handleTouchUp() {
        drawing = false;
    }

    public void render() {
        if (drawing) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            shapeRenderer.end();
        }
    }

    public int getX() {
        return (int) rectangle.x;
    }

    public int getY() {
        return (int) rectangle.y;
    }

    public int getWidth() {
        return (int) rectangle.width;
    }

    public int getHeight() {
        return (int) rectangle.height;
    }
}
