package com.alex.disisespain4;

import android.graphics.Color;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import java.io.IOException;

/**
 * Created by 48086820F on 19/05/2015.
 */
public class ResourcesManager {

    private static final ResourcesManager INSTANCE = new ResourcesManager();

    public Engine engine;
    public GameActivity activity;
    public BoundCamera camera;
    public VertexBufferObjectManager vbom;

    /* ----- SPLASH / LOGO ----- */
    public ITextureRegion splash_region;
    private BitmapTextureAtlas splashTextureAtlas;

    /* ----- MENU ----- */

    public ITextureRegion menu_background_region;
    public ITextureRegion play_region;
    public ITextureRegion options_region;

    private BuildableBitmapTextureAtlas menuTextureAtlas;

    /* ----- FONTS ----- */

    public Font font;
    public Font fontGameOver;
    public Font fontHUD;

    /* ----- OBJECTES I MONEDES ----- */

    // Textures del joc
    public BuildableBitmapTextureAtlas gameTextureAtlas;

    // Textures d'objectes partida

    public ITextureRegion fons_joc;
    public ITextureRegion platform1_zona;
    public ITextureRegion platform2_zona;
    public ITextureRegion euro_zona;
    public ITextureRegion poli_zona;
    public ITextureRegion martell_zona;
    public ITextureRegion corbata_zona;
    public ITextureRegion limit_zona;

    /* ----- TEXTURA JUGADOR ----- */

    public ITiledTextureRegion posicio_jugador;

    /* ----- Nivell Completat ----- */

    public ITextureRegion complete_window_region;
    public ITiledTextureRegion complete_stars_region;

    /* ----- MUSICA ----- */

    private Music music;

    public void loadMenuResources()
    {
        loadMenuGraphics();
        loadMenuAudio();
        loadMenuFonts();
    }

    public void loadGameResources()
    {
        loadGameGraphics();
        loadGameFonts();
        loadGameAudio();
    }

    private void loadMenuGraphics()
    {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
        menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_background.png");
        play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play.png");
        options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "options.png");

        try
        {
            this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.menuTextureAtlas.load();
        }
        catch (final ITextureAtlasBuilder.TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }
    }

    private void loadMenuAudio() {

    }

    private void loadGameGraphics() {

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/joc/");
        gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);

        /* ----- POSICIO JUGADOR ----- */

        posicio_jugador = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "jugador.png", 3, 1);

        /* ----- OBJECTES JOC ----- */

        platform1_zona = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform1.png");
        platform2_zona = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform2.png");
        euro_zona = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "euro.png");
        corbata_zona = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "corbata.png");
        complete_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "levelCompleteWindow.png");
        complete_stars_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "stars.png", 2, 1);
        poli_zona = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "poli.png");
        martell_zona = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "martell.png");
        fons_joc = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "fons.png");
        limit_zona = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "limit.png");

        try {
            this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
        } catch (final ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            Debug.e(e);
        }

        gameTextureAtlas.load();
    }

    private void loadGameFonts() {
        FontFactory.setAssetBasePath("gfx/font/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        final ITexture gameOverFont = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        fontHUD = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "Plump.ttf", 30, true, Color.WHITE, 2, Color.BLACK);
        fontGameOver = FontFactory.createStrokeFromAsset(activity.getFontManager(), gameOverFont, activity.getAssets(), "Plump.ttf", 70, true, Color.RED, 5, Color.BLACK);

        fontHUD.load();
        fontGameOver.load();
    }

    private void loadGameAudio() {
        /*MusicFactory.setAssetBasePath("gfx/");
        try {
            this.music = MusicFactory.createMusicFromAsset(this.engine.getMusicManager(), activity, "music.mp3");
            this.music.setLooping(true);
        } catch (final IOException e) {
            Debug.e(e);
        }*/
    }

    public void loadSplashScreen() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png", 0, 0);
        splashTextureAtlas.load();
    }

    public void unloadSplashScreen() {
        splashTextureAtlas.unload();
        splash_region = null;
    }

    public void unloadMenuTextures() {
        menuTextureAtlas.unload();
    }

    public void loadMenuTextures() {
        menuTextureAtlas.load();
    }

    /**
     * @param engine
     * @param activity
     * @param camera
     * @param vbom
     * <br><br>
     * We use this method at beginning of game loading, to prepare Resources Manager properly,
     * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
     */
    public static void prepareManager(Engine engine, GameActivity activity, BoundCamera camera, VertexBufferObjectManager vbom) {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }

    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }

    /* ----- FONT LOADING ----- */

    private void loadMenuFonts() {
        FontFactory.setAssetBasePath("gfx/font/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "Plump.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
        font.load();
    }

    /* ----- TORNAR AL MENU ----- */

    public void unloadGameTextures() {

    }
}
