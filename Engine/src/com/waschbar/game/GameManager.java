package com.waschbar.game;

import com.waschbar.engine.AbstractGame;
import com.waschbar.engine.GameContainer;
import com.waschbar.engine.Renderer;
import com.waschbar.engine.audio.SoundClip;
import com.waschbar.engine.gfx.Image;
import com.waschbar.engine.gfx.ImageTile;
import com.waschbar.engine.gfx.Light;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GameManager extends AbstractGame
{
    private int[] collision;
    private int levelW, levelH;
    private ArrayList<GameObject> objects = new ArrayList<GameObject>();

    public GameManager()
    {
        objects.add(new Player(2,2));
        loadLevel("/level.png");
    }

    public void init(GameContainer gc)
    {

    }

    @Override
    public void update(GameContainer gc, float dt)
    {
        for(int i = 0; i < objects.size(); i++)
        {
            objects.get(i).update(gc, dt);
            if(objects.get(i).isDead())
            {
                objects.remove(i);
                i--;
            }
        }
    }

    @Override
    public void render(GameContainer gc, Renderer renderer)
    {
        for(int x = 0; x < levelW; x++)
            for(int y = 0; y < levelH; y++)
            {
                if(collision[x + y * levelW] == 1)
                    renderer.drawFillRect(x*16, y*16, 16, 16, 0xff0f0f0f);
                else
                    renderer.drawFillRect(x*16, y*16, 16, 16, 0xfff9f9f9);

            }

        for(GameObject object : objects)
        {
            object.render(gc, renderer);
        }
    }

    public void loadLevel(String path)
    {
        Image levelImage = new Image(path);
        levelW = levelImage.getWidth();
        levelH = levelImage.getHeight();
        collision = new int[levelH * levelW];

        for(int x = 0; x < levelImage.getWidth(); x++)
            for(int y = 0; y < levelImage.getHeight(); y++)
            {
                int index = x + y * levelImage.getWidth();
                if(levelImage.getP()[index] == 0xff000000)
                {
                    collision[index] = 1;
                }
                else
                {
                    collision[index] = 0;
                }
            }
    }

    public static void main(String[] args)
    {
        GameContainer gc = new GameContainer(new GameManager());
        gc.setWidth(320);
        gc.setHeight(240);
        gc.setScale(3f);
        gc.start();
    }

}
