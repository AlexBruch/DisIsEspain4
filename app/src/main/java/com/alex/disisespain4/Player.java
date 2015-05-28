package com.alex.disisespain4;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by 48086820F on 25/05/2015.
 */
public abstract class Player extends AnimatedSprite {

    protected ResourcesManager resourcesManager;

    /* ----- JUGADOR ----- */

    private Body body;

    /* ----- MORT JUGADOR ----- */

    public abstract void onDie();

    /* ----- FER CORRER ----- */

    private boolean canRun = false;

    /* ----- CONSTRUCTOR ----- */

    public Player(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld) {
        super(pX, pY, ResourcesManager.getInstance().posicio_jugador, vbo);
        createPhysics(camera, physicsWorld);
        camera.setChaseEntity(this);
    }

    /* ----- FISIQUES JUGADOR ----- */

    private void createPhysics(final Camera camera, PhysicsWorld physicsWorld) {
        body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyDef.BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

        body.setUserData("player");
        body.setFixedRotation(true);

        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false) {
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                camera.onUpdate(0.1f);

                if (getY() <= 0) {
                    onDie();
                }

                if (canRun) {
                    body.setLinearVelocity(new Vector2(5, body.getLinearVelocity().y));
                }
            }
        });
    }

    /* ----- FUNCIO PER CORRER ----- */

    public void setRunning() {
        canRun = true;
        final long[] PLAYER_ANIMATE = new long[] {100, 100, 100};
        animate(PLAYER_ANIMATE, 0, 2, true);
    }

    /* ----- FUNCIO PER SALTAR ----- */

    public void jump(boolean salta) {
        if(salta) {
            body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 12));
        }
    }
}
