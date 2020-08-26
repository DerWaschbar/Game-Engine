package com.waschbar.game;

import com.waschbar.engine.AbstractGame;
import com.waschbar.engine.GameContainer;
import com.waschbar.engine.Renderer;
import com.waschbar.engine.gfx.Image;

import java.awt.event.KeyEvent;

public class GameManager extends AbstractGame
{

    private Image image;

    public GameManager()
    {
        image = new Image("/test.png");

        System.out.println(0xff00ff+" - " + 0xFF00FF);
    }

    @Override
    public void update(GameContainer gc, float dt)
    {
        if(gc.getInput().isKeyDown(KeyEvent.VK_A))
        {
            System.out.println("A");
        }
    }

    @Override
    public void render(GameContainer gc, Renderer renderer)
    {
        renderer.drawImage(image, gc.getInput().getMouseX(), gc.getInput().getMouseY());
    }

    public static void main(String[] args)
    {
        GameContainer gc = new GameContainer(new GameManager());
        gc.start();
    }

}
