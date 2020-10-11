package com.waschbar.game.components;

import com.waschbar.engine.GameContainer;
import com.waschbar.engine.Renderer;
import com.waschbar.game.GameManager;
import com.waschbar.game.Physics;
import com.waschbar.game.objects.GameObject;

public class AABBComponent extends Component
{
    private GameObject parent;
    private int centerX, centerY;
    private int halfWidth, halfHeight;

    public AABBComponent(GameObject parent)
    {
        this.parent = parent;
    }

    @Override
    public void update(GameContainer gc, GameManager gm, float dt)
    {
        centerX = (int) (parent.getPosX() + parent.getWidth() / 2);
        centerY = (int) (parent.getPosY() + parent.getHeight() / 2) + (parent.getPaddingTop() / 2);
        halfWidth = parent.getWidth() / 2 - parent.getPadding();
        halfHeight = parent.getHeight() / 2 - (parent.getPaddingTop() / 2);

        Physics.addAABBComponent(this);
    }

    @Override
    public void render(GameContainer gc, Renderer r)
    {
        r.drawRect(centerX-halfWidth,centerY-halfHeight,halfWidth*2,halfHeight*2,0xff000000);
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public int getHalfWidth() {
        return halfWidth;
    }

    public void setHalfWidth(int halfWidth) {
        this.halfWidth = halfWidth;
    }

    public int getHalfHeight() {
        return halfHeight;
    }

    public void setHalfHeight(int halfHeight) {
        this.halfHeight = halfHeight;
    }

    public GameObject getParent() {
        return parent;
    }

    public void setParent(GameObject parent) {
        this.parent = parent;
    }

}
