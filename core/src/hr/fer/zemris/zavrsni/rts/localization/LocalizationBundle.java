package hr.fer.zemris.zavrsni.rts.localization;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.I18NBundleLoader.I18NBundleParameter;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.I18NBundle;
import hr.fer.zemris.zavrsni.rts.common.IGameSettings;

import java.util.Locale;

public final class LocalizationBundle implements Disposable {

    private static final String BUNDLE = "localization/bundle";

    private LocalizationBundle() {}

    private static LocalizationBundle instance = new LocalizationBundle();

    private AssetManager assetManager;
    private I18NBundle bundle;

    public static LocalizationBundle getInstance() {
        return instance;
    }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void loadLocale(IGameSettings gameSettings) {
        Locale locale = Locale.forLanguageTag(gameSettings.getLocaleTag());

        if (assetManager.isLoaded(BUNDLE)) {
            assetManager.unload(BUNDLE);
        }

        assetManager.load(BUNDLE, I18NBundle.class, new I18NBundleParameter(locale));
        assetManager.finishLoading();

        bundle = assetManager.get(BUNDLE, I18NBundle.class);
    }

    public String getKey(String key) {
        return bundle.get(key);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
