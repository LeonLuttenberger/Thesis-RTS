package hr.fer.zemris.zavrsni.rts.util;

public final class Constants {

    private Constants() {}

    public static final float VIEWPORT_WIDTH = 640;
    public static final float VIEWPORT_HEIGHT = 480;
    public static final float VIEWPORT_GUI_WIDTH = 640;
    public static final float VIEWPORT_GUI_HEIGHT = 480;

    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;

    public static final String TEXTURE_ATLAS_OBJECTS = "images/textures.atlas";
    public static final String ARIAL_FONT = "data/arial-15.fnt";

    public static final String TILED_MAP_TMX = "level/desert.tmx";
    public static final String TERRAIN_LAYER = "Terrain Layer";
    public static final String OBJECT_LAYER = "Object Layer";

    public static final String TEXTURE_ATLAS_UI = "images/textures-ui.atlas";
    public static final String TEXTURE_ATLAS_LIBGDX_UI = "uiskin/star-soldier-ui.atlas";

    // Location of description file for skins
    public static final String SKIN_UI = "images/textures-ui.json";
    public static final String SKIN_LIBGDX_UI = "uiskin/star-soldier-ui.json";

    public static final String SETTINGS = "myrts.cfg";
}
