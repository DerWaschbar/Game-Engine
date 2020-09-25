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
    public static final int TS = 16;

    private boolean[] collision;
    private int levelW, levelH;
    private ArrayList<GameObject> objects = new ArrayList<GameObject>();
    private Camera camera;

    public GameManager()
    {
        objects.add(new Player(6,4));
        loadLevel("/level.png");
        camera = new Camera("player");
    }

    public void init(GameContainer gc)
    {

    }

    @Override
    public void update(GameContainer gc, float dt)
    {
        for(int i = 0; i < objects.size(); i++)
        {
            objects.get(i).update(gc,this, dt);
            if(objects.get(i).isDead())
            {
                objects.remove(i);
                i--;
            }
        }
        camera.update(gc, this, dt);
    }

    @Override
    public void render(GameContainer gc, Renderer renderer)
    {
        camera.render(renderer);
        for(int x = 0; x < levelW; x++)
            for(int y = 0; y < levelH; y++)
            {
                if(collision[x + y * levelW])
                    renderer.drawFillRect(x*TS, y*TS, TS, TS, 0xff0f0f0f);
                else
                    renderer.drawFillRect(x*TS, y*TS, TS, TS, 0xfff9f9f9);

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
        collision = new boolean[levelH * levelW];

        for(int x = 0; x < levelImage.getWidth(); x++)
            for(int y = 0; y < levelImage.getHeight(); y++)
            {
                int index = x + y * levelImage.getWidth();
                if(levelImage.getP()[index] == 0xff000000)
                {
                    collision[index] = true;
                }
                else
                {
                    collision[index] = false;
                }
            }
    }

    public void addObject(GameObject object) {
        objects.add(object);
    }

    public GameObject getObject(String tag) {
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getTag().equals(tag)) {
                return objects.get(i);
            }
        }
        return null;
    }

    public boolean getCollision(int x, int y) {
        if (x < 0 || x >= levelW || y < 0 || y > levelH)
            return true;
        return collision[x + y * levelW];
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
