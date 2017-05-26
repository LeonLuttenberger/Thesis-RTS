package hr.fer.zemris.zavrsni.rts.screen.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import hr.fer.zemris.zavrsni.rts.common.IGameState;
import hr.fer.zemris.zavrsni.rts.common.costs.Cost;

public class BuildButton extends TextButton {

    private final Cost cost;
    private final IGameState gameState;

    public BuildButton(Skin skin, String text, Cost cost, IGameState gameState) {
        super(text + "(" + cost.minerals + ")", skin);

        this.cost = cost;
        this.gameState = gameState;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (!cost.isSatisfied(gameState)) {
            setColor(Color.RED);
        } else {
            setColor(Color.BLACK);
        }
    }
}
