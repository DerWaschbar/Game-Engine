package com.waschbar.game.objects;

import com.waschbar.engine.GameContainer;
import com.waschbar.engine.Renderer;
import com.waschbar.game.GameManager;
import com.waschbar.game.objects.GameObject;

import static com.waschbar.game.GameManager.TS;

public class Bullet extends GameObject {

    private int tileX, tileY;
    private float offX, offY;
    private int direction;
    private int speed = 200;

    public Bullet(int tileX, int tileY, float offX, float offY, int direction) {
        this.direction = direction;
        this.tileX = tileX;
        this.tileY = tileY;
        this.offX = offX;
        this.offY = offY;
        this.width = 4;
        this.height = 4;
        this.padding = 0;
        this.paddingTop = 0;
        posX = tileX * TS + offX;
        posY = tileY * TS + offY;
    }

    @Override
    public void update(GameContainer gc, GameManager gm, float dt) {
        switch (direction) {
            case 0:
                offY -= speed * dt;
                break;
            case 1:
                offX += speed * dt;
                break;
            case 2:
                offY += speed * dt;
                break;
            case 3:
                offX -= speed * dt;
                break;
        }

        if (offY > TS) {
            tileY++;
            offY -= TS;
        }
        if (offY < 0) {
            tileY--;
            offY += TS;
        }
        if (offX > TS) {
            tileX++;
            offX -= TS;
        }
        if (offX < 0) {
            tileX--;
            offX += TS;
        }

        if (gm.getCollision(tileX, tileY)) {
            this.dead = true;
        }

        posX = tileX * TS + offX;
        posY = tileY * TS + offY;
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.drawFillRect((int)posX,  (int)posY, width, height, 0xffff0000);
    }

    @Override
    public void collision(GameObject other) {

    }
}
