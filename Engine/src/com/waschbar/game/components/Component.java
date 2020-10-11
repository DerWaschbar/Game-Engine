package com.waschbar.game.components;

import com.waschbar.engine.GameContainer;
import com.waschbar.engine.Renderer;
import com.waschbar.game.GameManager;
import com.waschbar.game.objects.GameObject;

public abstract class Component
{
    protected String tag;

    public abstract void update(GameContainer gc, GameManager gm, float dt);
    public abstract void render(GameContainer gc, Renderer r);

    public String getTag()
    {
        return tag;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
    }
}
