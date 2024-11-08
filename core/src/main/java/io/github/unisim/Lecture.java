package io.github.unisim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Lecture extends Building {
    public Lecture(FitViewport viewport, TiledMap tiledMap) {
        super(new Texture("lecture.png"), viewport, tiledMap);
    }
}
