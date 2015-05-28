package com.alex.disisespain4;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.xml.sax.Attributes;

import java.io.IOException;

/**
 * Created by 48086820F on 20/05/2015.
 */
public class GameScene extends BaseScene implements IOnSceneTouchListener {

    /* ----- HUD ----- */
    private HUD gameHUD;
    //private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_POPU = "popu";

    /* ----- Puntuacions ----- */
    private Text scoreText;
    public int score = 0;

    /* ----- FISIQUES ----- */
    private PhysicsWorld physicsWorld;

    /* ----- VARIABLES LOADER level ----- */

    private static final String TAG_ENTITY = "entity";
    private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
    private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
    private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";

    private static final Object TAG_PLATFORM1 = "platform1";
    private static final Object TAG_PLATFORM2 = "platform2";
    private static final Object TAG_EURO = "euro";
    private static final Object TAG_CORBATA = "corbata";
    private static final Object TAG_POLI = "poli";
    private static final Object TAG_MARTELL = "martell";
    private static final Object TAG_LIMIT = "limit";

    /* ----- TAG JUGADOR ----- */

    private static final Object TAG_PLAYER = "player";

    private Player player;

    /* ----- NIvell complert ----- */

    private LevelCompleteWindow levelCompleteWindow;
    private static final Object TAG_NIVELL_COMPLERT = "levelComplete";

    /* ----- MUERTE ----- */

    private Text gameOverText;
    private boolean gameOverDisplayed = false;

    /* ----- COMENCAR MOVIMENT ----- */

    private boolean firstTouch = false;

    @Override
    public void createScene() {
        createBackground();
        createHUD();
        createPhysics();
        loadLevel(1);
        setOnSceneTouchListener(this);
        createGameOverText();
        levelCompleteWindow = new LevelCompleteWindow(vbom);
    }

    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuScene(engine);
    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene() {
        camera.setHUD(null);
        camera.setCenter(480, 270);
        camera.setChaseEntity(null); // Eliminem el que segueix
    }

    /* ----- BACKGROUND ----- */

    private void createBackground() {
        //setBackground(new Background(Color.CYAN));
        int ample = 480;
        for(int x=0; x<7; x++) {

            attachChild(new Sprite(ample, 270, resourcesManager.fons_joc, vbom) // Posicio fons joc
            {
                @Override
                protected void preDraw(GLState pGLState, Camera pCamera) {
                    super.preDraw(pGLState, pCamera);
                    pGLState.enableDither();
                }
            });
            ample = ample + 959;
        }
    }

    /* ----- HUD ----- */

    private void createHUD() {
        gameHUD=new HUD();
        // Posicio text puntuacio
        scoreText = new Text(640, 510, resourcesManager.fontHUD, "1234567 en Suiza", new TextOptions(HorizontalAlign.LEFT), vbom);
        scoreText.setAnchorCenter(0, 0);
        scoreText.setText("0 en Suiza");
        gameHUD.attachChild(scoreText);

        camera.setHUD(gameHUD);
    }

    /* ----- CONTADOR PUNTUACIO ----- */

    private void addToScore(int i) {
        score += i;
        scoreText.setText(score + " en Suiza");
        if(score<0) {
            player.onDie();
        }
    }

    /* ----- FISIQUES ----- */

    private void createPhysics() {
        physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false);
        //physicsWorld.setContactListener(contactListener());
        registerUpdateHandler(physicsWorld);
    }

    /* ----- CARREGAR NIVELL XML ----- */

    private void loadLevel(int levelID) {
        final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);

        final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.0f, 0.5f);

        levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL) {
            public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException {
                final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
                final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);

                camera.setBounds(0, 0, width, height); // limits de la camara trets de l'XML
                camera.setBoundsEnabled(true);
                return GameScene.this;
            }
        });

        levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY) {
            @Override
            public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes,final SimpleLevelEntityLoaderData pEntityLoaderData) throws IOException {
                final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
                final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
                final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);

                final Sprite levelObject;

                if (type.equals(TAG_PLATFORM1)) {
                    levelObject = new Sprite(x, y, resourcesManager.platform1_zona, vbom);
                    PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyDef.BodyType.StaticBody, FIXTURE_DEF).setUserData("platform1");
                }
                else if (type.equals(TAG_PLATFORM2)) {
                    levelObject = new Sprite(x, y, resourcesManager.platform2_zona, vbom);
                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyDef.BodyType.StaticBody, FIXTURE_DEF);
                    body.setUserData("platform2");
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                }else if (type.equals(TAG_LIMIT)) {
                    levelObject = new Sprite(x, y, resourcesManager.limit_zona, vbom);
                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyDef.BodyType.StaticBody, FIXTURE_DEF);
                    body.setUserData("limit");
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                }else if (type.equals(TAG_EURO)) { // Creem euros
                    levelObject = new Sprite(x, y, resourcesManager.euro_zona, vbom) {
                        @Override
                        protected void onManagedUpdate(float pSecondsElapsed) {
                            super.onManagedUpdate(pSecondsElapsed);

                            if(player.collidesWith(this)) { // Si toquem una moneda
                                addToScore(1000);
                                this.setVisible(false);
                                this.setIgnoreUpdate(true);
                            }
                        }
                    };
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.5f)));
                }else if (type.equals(TAG_CORBATA)) { // Creem corbates
                    levelObject = new Sprite(x, y, resourcesManager.corbata_zona, vbom) {
                        @Override
                        protected void onManagedUpdate(float pSecondsElapsed) {
                            super.onManagedUpdate(pSecondsElapsed);

                            if(player.collidesWith(this)) { // Si toquem una corbata
                                addToScore(1500);
                                this.setVisible(false);
                                this.setIgnoreUpdate(true);
                            }
                        }
                    };
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                }else if (type.equals(TAG_POLI)) { // Creem policies
                    levelObject = new Sprite(x, y, resourcesManager.poli_zona, vbom) {
                        @Override
                        protected void onManagedUpdate(float pSecondsElapsed) {
                            super.onManagedUpdate(pSecondsElapsed);

                            if(player.collidesWith(this)) { // Si toquem un policia
                                addToScore(-500);
                                this.setVisible(false);
                                this.setIgnoreUpdate(true);
                            }
                        }
                    };
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                }
                else if (type.equals(TAG_MARTELL)) { // Creem policies
                    levelObject = new Sprite(x, y, resourcesManager.martell_zona, vbom) {
                        @Override
                        protected void onManagedUpdate(float pSecondsElapsed) {
                            super.onManagedUpdate(pSecondsElapsed);

                            if(player.collidesWith(this)) { // Si toquem un policia
                                addToScore(-5000);
                                this.setVisible(false);
                                this.setIgnoreUpdate(true);
                            }
                        }
                    };
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.5f)));
                }else if (type.equals(TAG_NIVELL_COMPLERT))
                {
                    levelObject = new Sprite(x, y, resourcesManager.complete_stars_region, vbom)
                    {
                        @Override
                        protected void onManagedUpdate(float pSecondsElapsed)
                        {
                            super.onManagedUpdate(pSecondsElapsed);

                            if (player.collidesWith(this))
                            {
                                levelCompleteWindow.display(LevelCompleteWindow.StarsCount.ONE, GameScene.this, camera);
                                this.setVisible(false);
                                this.setIgnoreUpdate(true);
                                gameOverDisplayed = true;
                            }
                        }
                    };
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                }else if (type.equals(TAG_PLAYER)) { // Posicionem el jugador a la pantalla
                    player = new Player(x, y, vbom, camera, physicsWorld) {
                        @Override
                        public void onDie()
                        {
                            if(!gameOverDisplayed) { // Si no es mostra acaba el joc
                                displayGameOverText();
                            }
                        }
                    };
                    levelObject = player;
                }else {
                    throw new IllegalArgumentException();
                }

                levelObject.setCullingEnabled(true);

                return levelObject;
            }
        });

        levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".xml");
    }

    /* El primer toc posa en funcionament la partida
     * la resta de tocs faran saltar el jugador */
    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionDown()) {
            if(!firstTouch) {
                player.setRunning();
                firstTouch = true;
            } else {
                if(score>=0) {
                    player.jump(true);
                }else {
                    player.jump(false);
                }
            }
        }
        return false;
    }

    /* ----- TEXT DERROTA ----- */
    private void createGameOverText() {
        gameOverText = new Text(0, 0, resourcesManager.fontGameOver, "Game Over", vbom);
    }
    private void displayGameOverText() {
        // Amaguem HUD
        camera.getHUD().setVisible(false);
        this.setIgnoreUpdate(true);

        // Disable camera chase entity
        camera.setChaseEntity(null);
        gameOverText.setPosition(camera.getCenterX(), camera.getCenterY());
        attachChild(gameOverText);
        gameOverDisplayed = true;
    }
}