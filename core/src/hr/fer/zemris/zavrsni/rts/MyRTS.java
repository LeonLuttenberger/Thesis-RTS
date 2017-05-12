package hr.fer.zemris.zavrsni.rts;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.strongjoshua.console.Console;
import com.strongjoshua.console.GUIConsole;
import hr.fer.zemris.zavrsni.rts.assets.Assets;
import hr.fer.zemris.zavrsni.rts.console.InGameCommandExecutor;
import hr.fer.zemris.zavrsni.rts.console.MyRTSCommandExecutor;
import hr.fer.zemris.zavrsni.rts.screen.GameScreen;
import hr.fer.zemris.zavrsni.rts.screen.MenuScreen;
import hr.fer.zemris.zavrsni.rts.util.Constants;
import hr.fer.zemris.zavrsni.rts.util.GameSettings;

public class MyRTS extends Game {

	private Console cheatConsole;
	private GameSettings settings;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		Assets.getInstance().init(new AssetManager());

		cheatConsole = new GUIConsole();
		cheatConsole.setSizePercent(100, 33);
		cheatConsole.setPositionPercent(0, 67);
		cheatConsole.setDisplayKeyID(Keys.GRAVE);

		settings = new GameSettings(Constants.SETTINGS);
		settings.load();

		setScreen(new MenuScreen(this, settings));
	}

	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);

		if (screen instanceof GameScreen) {
			GameScreen gameScreen = (GameScreen) screen;
			cheatConsole.setCommandExecutor(new InGameCommandExecutor(gameScreen.getController().getGameState()));
		} else {
			cheatConsole.setCommandExecutor(new MyRTSCommandExecutor());
		}

		cheatConsole.resetInputProcessing();
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

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		cheatConsole.refresh();
	}
}
