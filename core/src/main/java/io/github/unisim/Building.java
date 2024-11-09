package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
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

    /**
     * the constructor of this class initialises all the attributes
     *
     * @param viewport - the viewport for the game, sets the scale for the building's methods
     * @param tiledMap - the tileMap of the game allows for checks to be made on the types of tiles
     */
    public Building(FitViewport viewport, TiledMap tiledMap) {
        this.viewport = viewport; // viewport stored by reference
        this.tiledMap = tiledMap; // tileMap stored by reference
        setTexture();
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

    /**
     * sets the texture of the building
     */
    abstract void setTexture();

    /**
     * this method draws the sprite onto the screen when called
     * will first check if the current tile is a valid location
     *
     * @param spriteBatch the current batch of sprites being drawn
     */
    public void draw(SpriteBatch spriteBatch) {
        if (moving) {
            if (!validLocation(tiledMap)) { // if invalid location for building sprite colour changed
                buildingSprite.setColor(1, 0.5f, 0.5f, 1); // red tint
            } else {
                buildingSprite.setColor(Color.WHITE); // restores colour
            }
        }
        buildingSprite.draw(spriteBatch);
    }

    /**
     * if the building is moving and a click on the screen is performed this method will update the status of the
     * building if it has been clicked on
     *
     * if the location the building current is, is valid then moving is set to false and the tileMap is updated to show
     * that the tile is now filled
     *
     */
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

    /**
     * this method will allow the building to follow the mouse if it is supposed to be moving at the time
     *
     * if the building isn't moving the "construction" process is started. Starting a timer and changing between 4
     * different textures to represent the building taking time to be placed
     */
    public void logic() {
        if (moving) {
            follow();
        } else {
            if (!(timeElapsed > constructionTime + 1)) { // checks that the building is still under construction
                timeElapsed += Gdx.graphics.getDeltaTime(); // tracks the time in seconds
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

    /**
     * this method will change the current location of the building to mirror the mouse movements, whilst snapping to
     * the grid
     */
    public void follow() {
        Vector2 touchPos = new Vector2();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(touchPos); // converts mouse location to in-game units

        // moves the sprite to the new location snapping it to the grid of in-game units
        buildingSprite.setCenter((float) Math.floor(touchPos.x)+1, (float) Math.floor(touchPos.y)+1);

        float worldWidth = tiledMap.getProperties().get("width", Integer.class);
        float worldHeight = tiledMap.getProperties().get("height", Integer.class);
        float buildingWidth = buildingSprite.getWidth();
        float buildingHeight = buildingSprite.getHeight();

        // ensures that the sprites can't escape the map
        buildingSprite.setX(MathUtils.clamp(buildingSprite.getX(), 0, worldWidth - buildingWidth));
        buildingSprite.setY(MathUtils.clamp(buildingSprite.getY(), 0, worldHeight - buildingHeight));
    }

    /**
     * Must be provided with a {@Class TiledMap}
     * This class uses the "validityMap" layer of the tileMap, which stores the valid locations for buildings to be
     * on the map, to determine if the current location of the building would be valid for a placement
     *
     * @return boolean - True if location can have a building placed on that tile
     *                 - False otherwise
     */
    public boolean validLocation(TiledMap map) {
        final int validTileId = 50; // hard coded, bad
        final int invalidTileId = 51; // hard coded, bad
        final int occupiedTileId = 52; // hard coded, bad

        TiledMapTileLayer validityMap = (TiledMapTileLayer) tiledMap.getLayers().get("validitymap");
        // iterates through the 4 tiles that the building takes up
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                int currentTileId = validityMap.getCell((int) buildingSprite.getX()+x, (int) buildingSprite.getY()+y)
                    .getTile().getId();
                if(currentTileId != validTileId){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Must be provided with a {@Class TiledMap}
     * this method will change the tiles that the building is currently on to show that its occupied or has just been
     * freed up. This is done by accessing the "validityMap" layer of the tileMap.
     *
     * @param toBeOccupied - True if building is now occupying the space, false if the location is now being freed
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
