package com.alex.disisespain4;

import com.badlogic.gdx.math.Vector2;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;

/**
 * Created by 48086820F on 20/05/2015.
 */
public class GameScene extends BaseScene {

    /* ----- HUD ----- */
    private HUD gameHUD;

    /* ----- Puntuacions ----- */
    private Text scoreText;
    private int score = 0;

    /* ----- FISIQUES ----- */
    private PhysicsWorld physicsWorld;

    @Override
    public void createScene() {
        createBackground();
        createHUD();
        createPhysics();
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
    }

    /* ----- BACKGROUND ----- */

    private void createBackground() {
        setBackground(new Background(Color.BLUE));
    }

    /* ----- HUD ----- */

    private void createHUD() {
        gameHUD=new HUD();

        scoreText = new Text(10, 10, resourcesManager.font, "Score: 01234", new TextOptions(HorizontalAlign.LEFT), vbom);
        scoreText.setAnchorCenter(0, 0);
        scoreText.setText("Score: 0");
        gameHUD.attachChild(scoreText);

        camera.setHUD(gameHUD);
    }

    /* ----- CONTADOR PUNTUACIO ----- */

    private void addToScore(int i) {
        score += i;
        scoreText.setText("Score: " + score);
    }

    /* ----- FISIQUES ----- */

    private void createPhysics() {
        //physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false);
        //registerUpdateHandler(physicsWorld);
    }

}
