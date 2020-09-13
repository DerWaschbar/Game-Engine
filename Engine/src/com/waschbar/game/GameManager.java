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

public class GameManager extends AbstractGame
{

    private ImageTile imageTile;
    private Image image;
    private Image image1;
    private SoundClip clip;
    private Light light, light1;

    public GameManager()
    {
        image = new Image("/BlueSquares.png");
        image.setAlpha(true);
        image1 = new Image("/wood.png");
        image1.setLightBlock(Light.FULL);
        image1.setAlpha(true);
        imageTile = new ImageTile("/wood.png", 16, 16);
        imageTile.setAlpha(true);
        imageTile.setLightBlock(Light.FULL);
        clip = new SoundClip("/clap.wav");
        light = new Light(100, 0xff00ffff);
        light1 = new Light(100, 0xff00ffff);
        //System.out.println(0xff00ff+" - " + 0xFF00FF);
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
        renderer.setzDepth(0);
        renderer.drawImage(image, 0, 0);
        renderer.drawImage(image1, 100, 100);
        renderer.drawImage(imageTile, 200, 100);
        renderer.drawLight(light, gc.getInput().getMouseX(), gc.getInput().getMouseY());
        renderer.drawLight(light1, 200, 200);
        //renderer.drawImage(image, gc.getInput().getMouseX(), gc.getInput().getMouseY());
        //renderer.drawFillRect(30,10,32,20, Color.YELLOW.getRGB());
        //renderer.drawImageTile(image, gc.getInput().getMouseX(), gc.getInput().getMouseY(), (int)temp, 0);

        //renderer.process();
    }

    public static void main(String[] args)
    {
        GameContainer gc = new GameContainer(new GameManager());
        gc.start();
    }

}
