package hr.fer.zemris.zavrsni.rts;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.strongjoshua.console.Console;
import com.strongjoshua.console.GUIConsole;
import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.screen.GameScreen;

public class MyRTS extends Game {

	private Console cheatConsole;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		Assets.getInstance().init(new AssetManager());

		GameScreen gameScreen = new GameScreen(this);
		setScreen(gameScreen);

		cheatConsole = new GUIConsole();
		cheatConsole.setSizePercent(100, 33);
		cheatConsole.setPositionPercent(0, 67);
		cheatConsole.setDisplayKeyID(Keys.GRAVE);

		cheatConsole.setCommandExecutor(new CheatCommandExecutor(gameScreen.getController().getGameState()));
	}

	@Override
	public void render() {
		super.render();
		cheatConsole.draw();
	}

	@Override
	public void dispose() {
		Assets.getInstance().dispose();
		cheatConsole.dispose();
	}
}
