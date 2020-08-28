package com.waschbar.game;

import com.waschbar.engine.AbstractGame;
import com.waschbar.engine.GameContainer;
import com.waschbar.engine.Renderer;
import com.waschbar.engine.audio.SoundClip;
import com.waschbar.engine.gfx.Image;
import com.waschbar.engine.gfx.ImageTile;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameManager extends AbstractGame
{

    private ImageTile imageTile;
    private Image image;
    private SoundClip clip;

    public GameManager()
    {
        image = new Image("/light.png");
        image.setAlpha(true);
        imageTile = new ImageTile("/tile.png", 16, 16);
        clip = new SoundClip("/clap.wav");

        System.out.println(0xff00ff+" - " + 0xFF00FF);
    }

    @Override
    public void update(GameContainer gc, float dt)
    {
        if(gc.getInput().isKeyDown(KeyEvent.VK_A))
        {
            clip.play();
        }

        temp += dt * 5;
        if(temp > 4)
                temp = 0;
    }

    float temp = 0;

    @Override
    public void render(GameContainer gc, Renderer renderer)
    {
        for(int x = 0; x < image.getWidth(); x++)
            for(int y = 0; y < image.getHeight(); y++)
                renderer.setLightMap(x, y, image.getP()[x+y*image.getWidth()]);

        renderer.drawImage(image, gc.getInput().getMouseX(), gc.getInput().getMouseY());
        renderer.drawFillRect(30,10,32,20, Color.YELLOW.getRGB());
        //renderer.drawImageTile(image, gc.getInput().getMouseX(), gc.getInput().getMouseY(), (int)temp, 0);

        //renderer.process();
    }

    public static void main(String[] args)
    {
        GameContainer gc = new GameContainer(new GameManager());
        gc.start();
    }

}
