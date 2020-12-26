package com.waschbar.engine;

import com.waschbar.engine.GameContainer;
import com.waschbar.engine.Renderer;
import com.waschbar.game.GameManager;
import com.waschbar.game.objects.GameObject;

import static com.waschbar.game.GameManager.TS;

public class Camera {

    private float offX, offY;

    private GameObject target = null;
    private String targetTag;

    public Camera(String tag) {
        this.targetTag = tag;
    }

    public void update(GameContainer gc, GameManager gm, float dt) {
        if (target == null) {
            target = gm.getObject(targetTag);
        }
        if (target == null) {
            return;
        }

        offX = (target.getPosX() + target.getWidth() / 2) - gc.getWidth() / 2;
        offY = (target.getPosY() + target.getHeight() / 2) - gc.getHeight() / 2;

        if (offX < 0)
            offX = 0;
        if (offY < 0)
            offY = 0;
        if (offX + gc.getWidth() > gm.getLevelW() * TS)
            offX = gm.getLevelW() * TS - gc.getWidth();
        if (offY + gc.getHeight() > gm.getLevelH() * TS)
            offY = gm.getLevelH() * TS - gc.getHeight();
    }

    public void render(Renderer r) {
        r.setCamX((int)offX);
        r.setCamY((int)offY);
    }

    public float getOffX() {
        return offX;
    }

    public void setOffX(float offX) {
        this.offX = offX;
    }

    public float getOffY() {
        return offY;
    }

    public void setOffY(float offY) {
        this.offY = offY;
    }

    public GameObject getTarget() {
        return target;
    }

    public void setTarget(GameObject target) {
        this.target = target;
    }

    public String getTargetTag() {
        return targetTag;
    }

    public void setTargetTag(String targetTag) {
        this.targetTag = targetTag;
    }
}
