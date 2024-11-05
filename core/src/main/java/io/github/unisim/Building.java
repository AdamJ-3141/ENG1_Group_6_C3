package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Building {
    Sprite buildingSprite;
    boolean moving;
    Rectangle bucketRectangle;

    public Building(Texture theTexture) {
        buildingSprite = new Sprite(theTexture);
        buildingSprite.setSize(1, 1);
        bucketRectangle = new Rectangle();
        moving = false;
    }

    public void draw(SpriteBatch spriteBatch) {
        buildingSprite.draw(spriteBatch);
    }

    public void input(FitViewport viewport) {
        if (Gdx.input.justTouched()) { //makes one event per mouse touch
            float bucketWidth = buildingSprite.getWidth();
            float bucketHeight = buildingSprite.getHeight();
            bucketRectangle.set(buildingSprite.getX(), buildingSprite.getY(), bucketWidth, bucketHeight);

            Vector2 touchPos = new Vector2();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos); //converts to in-game units

            if (bucketRectangle.contains(touchPos.x, touchPos.y)) {
                moving = !moving;
            }
        }
    }

    public void logic(FitViewport viewport) {
        if (moving) {
            follow(viewport);
        }
    }

    public void follow(FitViewport viewport) {
        Vector2 touchPos = new Vector2();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(touchPos); //converts to in-game units
        buildingSprite.setCenter((float) Math.floor(touchPos.x)+0.5f, (float) Math.floor(touchPos.y)+0.5f);

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float bucketWidth = buildingSprite.getWidth();
        float bucketHeight = buildingSprite.getHeight();

        buildingSprite.setX(MathUtils.clamp(buildingSprite.getX(), 0, worldWidth - bucketWidth));
        buildingSprite.setY(MathUtils.clamp(buildingSprite.getY(), 0, worldHeight - bucketHeight));
    }
}
