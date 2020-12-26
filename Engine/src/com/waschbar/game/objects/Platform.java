package com.waschbar.game.objects;

import com.waschbar.engine.GameContainer;
import com.waschbar.engine.Renderer;
import com.waschbar.game.GameManager;
import com.waschbar.engine.components.AABBComponent;

public class Platform extends GameObject {

    private int color = (int) (Math.random() * Integer.MAX_VALUE);
    public Platform(int x, int y)
    {
        this.tag = "platform";
        this.width = 64;
        this.height = 16;
        this.posX = x;
        this.posY = y;

        this.addComponent(new AABBComponent(this));
    }

    @Override
    public void update(GameContainer gc, GameManager gm, float dt) {
        this.updateComponents(gc, gm, dt);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.drawFillRect((int)posX, (int)posY, width, height, color);
        this.renderComponents(gc, r);
    }

    @Override
    public void collision(GameObject other) {
        color = (int) (Math.random() * Integer.MAX_VALUE);
    }
}
