package io.github.unisim;

import com.badlogic.gdx.graphics.Texture;

public class Accommodation extends Building {
    public Accommodation() {
        super(new Texture("accommodation.png"));
        buildingSprite.setCenter(1,0);
    }
}
