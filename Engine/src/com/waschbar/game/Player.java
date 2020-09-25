package com.waschbar.game;

import com.waschbar.engine.GameContainer;
import com.waschbar.engine.Renderer;

import java.awt.event.KeyEvent;

import static com.waschbar.game.GameManager.TS;

public class Player extends GameObject
{
    private int tileX, tileY;
    private float offX, offY;

    private float speed = 100;          // X speed
    private float fallSpeed = 350;       // g
    private float jump = (float) -250.0; // Up Speed
    private float fallDistance = 0; // Y speed
    private boolean ground = false;

    public Player(int posX, int posY)
    {
        this.tag = "player";
        this.tileX = posX;
        this.tileY = posY;
        this.offX = 0;
        this.offY = 0;
        this.posX = posX * TS;
        this.posY = posY * TS;
        this.width = TS - 1;
        this.height = TS - 1;
    }

    public void update(GameContainer gc, GameManager gm, float dt)
    {
        //Left and right
        if (gc.getInput().isKey(KeyEvent.VK_D)) {
            if (gm.getCollision(tileX + 1, tileY) || gm.getCollision(tileX + 1,
                    tileY + (int)Math.signum((int)offY))) {
                if (offX < 0) {
                    offX += dt * speed;
                    if (offX > 0) {
                        offX = 0;
                    }
                }
                else {
                    offX = 0;
                }
            }
            else {
                offX += dt * speed;
            }
        }

        if (gc.getInput().isKey(KeyEvent.VK_A)) {
            if (gm.getCollision(tileX - 1, tileY) || gm.getCollision(tileX - 1,
                    tileY + (int)Math.signum((int)offY))) {
                if (offX > 0) {
                    offX -= dt * speed;
                    if (offX < 0) {
                        offX = 0;
                    }
                }
                else {
                    offX = 0;
                }
            }
            else {
                offX -= dt * speed;
            }
        }
        //End of left and right

        //Jump and gravity
        fallDistance +=  dt * fallSpeed;
        if (gc.getInput().isKeyDown(KeyEvent.VK_W) && ground) {
            fallDistance = jump;
            ground = false;
        }

        //System.out.println(offY + " v: " + fallDistance + " a: " + dt * fallSpeed);
        offY += dt * fallDistance;

        if (fallDistance < 0) {
            if ((gm.getCollision(tileX, tileY - 1) || gm.getCollision(tileX +
                    (int) Math.signum((int) offX), tileY - 1)) && offY < 0) {
                fallDistance = 0;
                offY = 0;
            }
        }

        if (fallDistance > 0) {
            if ((gm.getCollision(tileX, tileY + 1) || gm.getCollision(tileX +
                    (int) Math.signum((int) offX), tileY + 1)) && offY > 0) {
                fallDistance = 0;
                offY = 0;
                ground = true;
            }
        }
        //End of jump and gravity

        //Final position
        if (offY > TS / 2.0) {
            tileY++;
            offY -= TS;
        }
        if (offY < -TS / 2.0) {
            tileY--;
            offY += TS;
        }
        if (offX > TS / 2.0) {
            tileX++;
            offX -= TS;
        }
        if (offX < -TS / 2.0) {
            tileX--;
            offX += TS;
        }
        posX = tileX * TS + offX;
        posY = tileY * TS + offY;
    }

    public void render(GameContainer gc, Renderer r)
    {
        r.drawFillRect((int)posX, (int)posY, width, height, 0xff00ff00);
    }

}
