package com.waschbar.game.objects;

import com.waschbar.engine.GameContainer;
import com.waschbar.engine.Renderer;
import com.waschbar.game.GameManager;
import com.waschbar.game.components.Component;

import java.util.ArrayList;

public abstract class GameObject
{
    protected String tag;
    protected float posX, posY;
    protected int width, height;
    protected int padding, paddingTop;
    protected boolean dead = false;

    protected ArrayList<Component> components = new ArrayList<Component>();

    public abstract void update(GameContainer gc, GameManager gm, float dt);
    public abstract void render(GameContainer gc, Renderer r);
    public abstract void collision(GameObject other);

    public void updateComponents(GameContainer gc, GameManager gm, float dt)
    {
        for(Component c : components)
            c.update(gc, gm, dt);
    }

    public void renderComponents(GameContainer gc, Renderer r)
    {
        for(Component c : components)
            c.render(gc, r);
    }

    public void addComponent(Component c)
    {
        components.add(c);
    }

    public void removeComponent(String tag)
    {
        for(Component c : components)
            if(c.getTag().equalsIgnoreCase(tag))
                components.remove(c);
    }

    public Component findComponent(String tag)
    {
        for(Component c : components)
            if(c.getTag().equalsIgnoreCase(tag))
                return c;
        return null;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }
}
