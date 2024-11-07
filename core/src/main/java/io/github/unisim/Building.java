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
    Texture buildingTexture;
    Texture construction1;
    Texture construction2;
    Texture construction3;
    Texture construction4;
    float timeElapsed;
    final float constructionTime;

    public Building(Texture theTexture, FitViewport viewport) {
        this.viewport = viewport; // viewport stored by reference
        buildingTexture = theTexture;
        construction1 = theTexture; // to be replaced with correct textures
        construction2 = theTexture;
        construction3 = theTexture;
        construction4 = theTexture;
        buildingSprite = new Sprite(buildingTexture);
        buildingSprite.setSize(1, 1);
        bucketRectangle = new Rectangle();
        moving = true; // starts the building as being able to move
        timeElapsed = 0;
        constructionTime = 4; // determines how many seconds it takes to finish construction of a building
    }

    public void draw(SpriteBatch spriteBatch) {
        if (moving) {
            if (!validLocation()) { // if invalid location for building sprite colour changed
                buildingSprite.setColor(1, 0.5f, 0.5f, 1); // red tint
            } else {
                buildingSprite.setColor(Color.WHITE); // restore colour
            }
        }
        buildingSprite.draw(spriteBatch);
    }

    public void input() {
        if (moving) {
            if (Gdx.input.justTouched()) { //makes one event per mouse touch
                // creates up-to-date rectangle
                float bucketWidth = buildingSprite.getWidth();
                float bucketHeight = buildingSprite.getHeight();
                bucketRectangle.set(buildingSprite.getX(), buildingSprite.getY(), bucketWidth, bucketHeight);

                Vector2 touchPos = new Vector2();
                touchPos.set(Gdx.input.getX(), Gdx.input.getY());
                viewport.unproject(touchPos); //converts mouse location to in-game units

                if (bucketRectangle.contains(touchPos.x, touchPos.y)) {
                    if (validLocation()) { // building is placed
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
        else {
            if (!(timeElapsed > constructionTime + 1)) {
                timeElapsed += Gdx.graphics.getDeltaTime();
                if (Math.floor(timeElapsed) == 0) {
                    buildingSprite.setTexture(buildingTexture); // start of construction
                } else if (Math.floor(timeElapsed) == constructionTime * 0.25) {
                    buildingSprite.setTexture(buildingTexture); // 1/4
                } else if (Math.floor(timeElapsed) == constructionTime * 0.5) {
                    buildingSprite.setTexture(buildingTexture); // 2/4
                } else if (Math.floor(timeElapsed) == constructionTime * 0.75) {
                    buildingSprite.setTexture(buildingTexture); // 3/4
                } else {
                    buildingSprite.setTexture(buildingTexture); // restore to normal texture once built
                }
            }
        }
    }

    public void follow() {
        Vector2 touchPos = new Vector2();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(touchPos); //converts mouse location to in-game units
        // moves the sprite to the new location snapping it to the grid of in-game units
        buildingSprite.setCenter((float) Math.floor(touchPos.x)+0.5f, (float) Math.floor(touchPos.y)+0.5f);

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float bucketWidth = buildingSprite.getWidth();
        float bucketHeight = buildingSprite.getHeight();

        // ensures no escaping sprites
        buildingSprite.setX(MathUtils.clamp(buildingSprite.getX(), 0, worldWidth - bucketWidth));
        buildingSprite.setY(MathUtils.clamp(buildingSprite.getY(), 0, worldHeight - bucketHeight));
    }

    public boolean validLocation() {
        //needs logic to determine if location is valid place for building
        return true;
    }
}
