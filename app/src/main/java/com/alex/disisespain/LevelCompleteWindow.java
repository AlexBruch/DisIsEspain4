package com.alex.disisespain;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by 48086820F on 26/05/2015.
 */
public class LevelCompleteWindow extends Sprite {
    private TiledSprite chorizo1;
    private TiledSprite chprizo2;
    private TiledSprite chorizo3;

    public enum StarsCount
    {
        ONE,
        TWO,
        THREE
    }

    public LevelCompleteWindow(VertexBufferObjectManager pSpriteVertexBufferObject)
    {
        super(0, 0, 650, 400, ResourcesManager.getInstance().complete_window_region, pSpriteVertexBufferObject);
        attachStars(pSpriteVertexBufferObject);
    }

    private void attachStars(VertexBufferObjectManager pSpriteVertexBufferObject)
    {
        chorizo1 = new TiledSprite(170, 110, ResourcesManager.getInstance().complete_chorizo_region, pSpriteVertexBufferObject);
        chprizo2 = new TiledSprite(325, 110, ResourcesManager.getInstance().complete_chorizo_region, pSpriteVertexBufferObject);
        chorizo3 = new TiledSprite(480, 110, ResourcesManager.getInstance().complete_chorizo_region, pSpriteVertexBufferObject);

        attachChild(chorizo1);
        attachChild(chprizo2);
        attachChild(chorizo3);
    }

    public void display(StarsCount starsCount, Scene scene, Camera camera)
    {
        // Canviem la quantitat de choriços segons el nivell
        switch (starsCount)
        {
            case ONE:
                chorizo1.setCurrentTileIndex(0);
                chprizo2.setCurrentTileIndex(1);
                chorizo3.setCurrentTileIndex(1);
                break;
            case TWO:
                chorizo1.setCurrentTileIndex(0);
                chprizo2.setCurrentTileIndex(0);
                chorizo3.setCurrentTileIndex(1);
                break;
            case THREE:
                chorizo1.setCurrentTileIndex(0);
                chprizo2.setCurrentTileIndex(0);
                chorizo3.setCurrentTileIndex(0);
                break;
        }

        // Amaguem HUD
        camera.getHUD().setVisible(false);

        // Aturem camera
        camera.setChaseEntity(null);

        // Posem el cartell de nivell complert al centre
        setPosition(camera.getCenterX(), camera.getCenterY());
        scene.attachChild(this);
    }
}
