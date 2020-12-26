package com.framework.engine.components;

import com.framework.engine.GameContainer;
import com.framework.engine.Renderer;
import com.framework.game.GameManager;

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
