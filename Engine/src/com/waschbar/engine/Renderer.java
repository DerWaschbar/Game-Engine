package com.waschbar.engine;

import com.waschbar.engine.gfx.Image;

import java.awt.*;
import java.awt.image.DataBufferInt;

public class Renderer
{
    private int pW, pH; // PixelWidth, Pixel Height
    private int[] p;

    public Renderer(GameContainer gc)
    {
        pW = gc.getWidth();
        pH = gc.getHeight();
        p = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
    }

    public void clear()
    {
        for(int i = 0; i < p.length; i++)
        {
            p[i] = 0;
        }
    }

    public void setPixel(int x, int y, int value)
    {
        if((x < 0 || x >= pW || y < 0 || y > pH) || value == (new Color(255,0,255)).getRGB())
            return;

        p[x + y * pW] = value;
    }

    public void drawImage(Image image, int offsetX, int offsetY)
    {
        for(int x = 0; x < image.getWidth(); x++)
            for(int y = 0; y < image.getHeight(); y++)
            {
                setPixel(x + offsetX, y+offsetY, image.getP()[x + y * image.getWidth()]);
            }
    }

}
