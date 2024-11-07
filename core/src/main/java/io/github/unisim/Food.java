package io.github.unisim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Food extends Building {
    public Food(FitViewport viewport) {
        super(new Texture("food.png"), viewport);
        buildingSprite.setCenter(1,1);
    }
}
