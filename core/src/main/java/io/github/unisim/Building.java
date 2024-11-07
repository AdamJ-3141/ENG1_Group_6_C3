package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
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
    FitViewport viewport;

    public Building(Texture theTexture, FitViewport viewport) {
        this.viewport = viewport;
        buildingSprite = new Sprite(theTexture);
        buildingSprite.setSize(1, 1);
        bucketRectangle = new Rectangle();
        moving = true;
    }

    public void draw(SpriteBatch spriteBatch) {
        if (moving) {
            if (!validLocation()) {
                buildingSprite.setColor(1, 0.5f, 0.5f, 1);
            } else {
                buildingSprite.setColor(Color.WHITE);
            }
        }
        buildingSprite.draw(spriteBatch);
    }

    public void input() {
        if (moving) {
            if (Gdx.input.justTouched()) { //makes one event per mouse touch
                float bucketWidth = buildingSprite.getWidth();
                float bucketHeight = buildingSprite.getHeight();
                bucketRectangle.set(buildingSprite.getX(), buildingSprite.getY(), bucketWidth, bucketHeight);

                Vector2 touchPos = new Vector2();
                touchPos.set(Gdx.input.getX(), Gdx.input.getY());
                viewport.unproject(touchPos); //converts to in-game units

                if (bucketRectangle.contains(touchPos.x, touchPos.y)) {
                    if (validLocation()) {
                        moving = false;
                        buildingSprite.setColor(Color.WHITE);
                    }
                }
            }
        }
    }

    public void logic() {
        if (moving) {
            follow();
        }
    }

    public void follow() {
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

    public boolean validLocation() {
        //needs logic to determine if location is valid place for building
        return true;
    }
}
