package com.waschbar.game;

import com.waschbar.engine.AbstractGame;
import com.waschbar.engine.GameContainer;
import com.waschbar.engine.Renderer;
import com.waschbar.engine.gfx.Image;
import com.waschbar.engine.gfx.ImageTile;

import java.awt.event.KeyEvent;

public class GameManager extends AbstractGame
{

    private ImageTile image;

    public GameManager()
    {
        image = new ImageTile("/tile.png", 16, 16);

        System.out.println(0xff00ff+" - " + 0xFF00FF);
    }

    @Override
    public void update(GameContainer gc, float dt)
    {
        if(gc.getInput().isKeyDown(KeyEvent.VK_A))
        {
            System.out.println("A");
        }

        temp += dt * 5;
        if(temp > 4)
                temp = 0;
    }

    float temp = 0;

    @Override
    public void render(GameContainer gc, Renderer renderer)
    {
        renderer.drawImageTile(image, gc.getInput().getMouseX(), gc.getInput().getMouseY(), (int)temp, 0);
    }

    public static void main(String[] args)
    {
        GameContainer gc = new GameContainer(new GameManager());
        gc.start();
    }

}
