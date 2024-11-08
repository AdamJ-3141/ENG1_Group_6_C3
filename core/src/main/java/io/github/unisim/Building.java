package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

abstract class Building {
    Sprite buildingSprite;
    boolean moving;
    Rectangle buildingRectangle;
    FitViewport viewport;
    TiledMap tiledMap;
    Texture buildingTexture;
    Texture construction1;
    Texture construction2;
    Texture construction3;
    Texture construction4;
    float timeElapsed;
    final float constructionTime;

    public Building(Texture theTexture, FitViewport viewport, TiledMap tiledMap) {
        this.viewport = viewport; // viewport stored by reference
        this.tiledMap = tiledMap;
        buildingTexture = theTexture;
        construction1 = theTexture; // to be replaced with correct textures
        construction2 = theTexture;
        construction3 = theTexture;
        construction4 = theTexture;
        buildingSprite = new Sprite(buildingTexture);
        buildingSprite.setSize(1, 1);
        buildingRectangle = new Rectangle();
        moving = true; // starts the building as being able to move
        timeElapsed = 0;
        constructionTime = 4; // determines how many seconds it takes to finish construction of a building
    }

    public void draw(SpriteBatch spriteBatch) {
        if (moving) {
            if (!validLocation(tiledMap)) { // if invalid location for building sprite colour changed
                buildingSprite.setColor(1, 0.5f, 0.5f, 1); // red tint
            } else {
                buildingSprite.setColor(Color.WHITE); // restore colour
            }
        }
        buildingSprite.draw(spriteBatch);
    }

    public void input() {
        if (moving) {
            if (Gdx.input.justTouched()) { // makes one event per mouse touch
                // creates up-to-date rectangle
                float buildingWidth = buildingSprite.getWidth();
                float buildingHeight = buildingSprite.getHeight();
                buildingRectangle.set(buildingSprite.getX(), buildingSprite.getY(), buildingWidth, buildingHeight);

                Vector2 touchPos = new Vector2();
                touchPos.set(Gdx.input.getX(), Gdx.input.getY());
                viewport.unproject(touchPos); // converts mouse location to in-game units

                if (buildingRectangle.contains(touchPos.x, touchPos.y)) {
                    if (validLocation(tiledMap)) { // building is placed
                        moving = false;
                        buildingSprite.setColor(Color.WHITE);
                        markLocation(tiledMap, true);
                    }
                }
            }
        }
    }

    public void logic() {
        if (moving) {
            follow();
        } else {
            if (!(timeElapsed > constructionTime + 1)) {
                timeElapsed += Gdx.graphics.getDeltaTime();
                if (Math.floor(timeElapsed) == 0) {
                    buildingSprite.setTexture(construction1); // start of construction
                } else if (Math.floor(timeElapsed) == constructionTime * 0.25) {
                    buildingSprite.setTexture(construction2); // 1/4
                } else if (Math.floor(timeElapsed) == constructionTime * 0.5) {
                    buildingSprite.setTexture(construction3); // 2/4
                } else if (Math.floor(timeElapsed) == constructionTime * 0.75) {
                    buildingSprite.setTexture(construction4); // 3/4
                } else {
                    buildingSprite.setTexture(buildingTexture); // restore to normal texture once built
                }
            }
        }
    }

    public void follow() {
        Vector2 touchPos = new Vector2();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(touchPos); // converts mouse location to in-game units
        // moves the sprite to the new location snapping it to the grid of in-game units
        buildingSprite.setCenter((float) Math.floor(touchPos.x) + 0.5f, (float) Math.floor(touchPos.y) + 0.5f);

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float buildingWidth = buildingSprite.getWidth();
        float buildingHeight = buildingSprite.getHeight();

        // ensures no escaping sprites
        buildingSprite.setX(MathUtils.clamp(buildingSprite.getX(), 0, worldWidth - buildingWidth));
        buildingSprite.setY(MathUtils.clamp(buildingSprite.getY(), 0, worldHeight - buildingHeight));
    }

    /**
     * Must be provided with a {@Class TiledMap}
     *
     * @return
     */
    public boolean validLocation(TiledMap map) {
        final int validTileId = 50; // hard coded, bad
        final int invalidTileId = 51; // hard coded, bad
        final int occupiedTileId = 52; // hard coded, bad

        TiledMapTileLayer validityMap = (TiledMapTileLayer) tiledMap.getLayers().get("validitymap");
        int currentTileId = validityMap.getCell((int) buildingSprite.getX(), (int) buildingSprite.getY()).getTile()
                .getId();

        return currentTileId == validTileId;
    }

    /**
     * Must be provided with a {@Class TiledMap}
     *
     * @return
     */
    public void markLocation(TiledMap map, boolean toBeOccupied) {
        final int validTileId = 50; // hard coded, bad
        final int invalidTileId = 51; // hard coded, bad
        final int occupiedTileId = 52; // hard coded, bad

        TiledMapTileLayer validityMap = (TiledMapTileLayer) tiledMap.getLayers().get("validitymap");
        TiledMapTile currentTile = validityMap.getCell((int) buildingSprite.getX(), (int) buildingSprite.getY())
                .getTile();
        if (toBeOccupied) {
            currentTile.setId(occupiedTileId);
        } else {
            currentTile.setId(validTileId);
        }
    }
}
