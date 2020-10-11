package com.waschbar.game.objects;

import com.waschbar.engine.GameContainer;
import com.waschbar.engine.Renderer;
import com.waschbar.game.GameManager;
import com.waschbar.game.components.AABBComponent;

public class Platform extends GameObject {

    private int color = (int) (Math.random() * Integer.MAX_VALUE);
    public Platform()
    {
        this.tag = "platfrom";
        this.width = 32;
        this.height = 16;
        this.posX = 176;
        this.posY = 160;

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
