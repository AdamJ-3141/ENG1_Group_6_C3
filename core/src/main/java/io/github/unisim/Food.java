package io.github.unisim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Food extends Building {
    public Food(FitViewport viewport, TiledMap tiledMap) {
        super(new Texture("food.png"), viewport, tiledMap);
    }
}
