package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

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
        construction1 = new Texture("construction1.png");
        construction2 = new Texture("construction2.png");
        construction3 = new Texture("construction3.png");
        construction4 = new Texture("construction4.png");
        buildingSprite = new Sprite(buildingTexture);
        buildingSprite.setSize(2, 2);
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
                    buildingSprite.setSize(2, 2);
                }
            }
        }
    }

    public void follow() {
        Vector2 touchPos = new Vector2();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(touchPos); // converts mouse location to in-game units
        // moves the sprite to the new location snapping it to the grid of in-game units
        buildingSprite.setCenter((float) Math.floor(touchPos.x)+1, (float) Math.floor(touchPos.y)+1);

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
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                int currentTileId = validityMap.getCell((int) buildingSprite.getX()+x, (int) buildingSprite.getY()+y).getTile()
                    .getId();
                if(currentTileId != validTileId){
                    return false;
                }
            }
        }
        return true;
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
        TiledMapTileSet tileSet = map.getTileSets().getTileSet("validitytiles");
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                TiledMapTileLayer.Cell currentCell = validityMap.getCell((int) buildingSprite.getX() + x, (int) buildingSprite.getY() + y);
                if (toBeOccupied) {
                    currentCell.setTile(tileSet.getTile(occupiedTileId));
                } else {
                    currentCell.setTile(tileSet.getTile(validTileId));
                }
            }
        }
    }
}
