package hr.fer.zemris.zavrsni.rts.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import hr.fer.zemris.zavrsni.rts.MyRTS;
import hr.fer.zemris.zavrsni.rts.util.Constants;

public class DesktopLauncher {

	private static boolean rebuildAtlas = true;
	private static boolean drawDebugOutline = false;

	public static void main (String[] arg) {
		if (rebuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;

			TexturePacker.processIfModified(
					settings,
					"../assets-raw/images",
					"images",
					"textures"
			);

			TexturePacker.processIfModified(
					settings,
					"../assets-raw/images-ui",
					"images",
					"textures-ui"
			);
		}

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) Constants.VIEWPORT_GUI_WIDTH;
		config.height = (int) Constants.VIEWPORT_GUI_HEIGHT;

		config.title = "My RTS";

		new LwjglApplication(new MyRTS(), config);
	}
}
