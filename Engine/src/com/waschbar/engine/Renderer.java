package com.waschbar.engine;

import com.waschbar.engine.gfx.Image;
import com.waschbar.engine.gfx.ImageTile;
import com.waschbar.engine.gfx.Font;

import java.awt.*;
import java.awt.image.DataBufferInt;

public class Renderer
{
    private int pW, pH; // PixelWidth, Pixel Height
    private int[] p;

    private Font font = Font.STANDARD;

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
        for(int x = Math.max(offsetX, 0); x < Math.min(image.getWidth()+offsetX, pW); x++)
            for(int y = Math.max(offsetY, 0); y < Math.min(image.getHeight()+offsetY, pH); y++)
            {
                setPixel(x, y, image.getP()[(x-offsetX) + (y-offsetY) * image.getWidth()]);
            }
    }

    public void drawImageTile(ImageTile image, int offsetX, int offsetY, int tileX, int tileY)
    {
        for(int x = Math.max(offsetX, 0); x < Math.min(image.getTileWidth()+offsetX, pW); x++)
            for(int y = Math.max(offsetY, 0); y < Math.min(image.getTileHeight()+offsetY, pH); y++)
            {
                setPixel(x, y, image.getP()[(x-offsetX+tileX*image.getTileWidth()) + (y-offsetY+tileY*image.getTileHeight()) * image.getWidth()]);
            }
    }

    public void drawText(String text, int offX, int offY, int color)
    {
        Image fontImg = font.getFontImage();
        text = text.toUpperCase();
        int offset = 0;

        for(int i = 0; i < text.length(); i++)
        {
            int unicode = text.codePointAt(i) - 32;

            for(int x = 0; x < font.getWidths()[unicode]; x++)
                for(int y = 0; y < fontImg.getHeight(); y++)
                {
                    if(fontImg.getP()[(x + font.getOffsets()[unicode]) + y * fontImg.getWidth()] == 0xffffffff)
                    {
                        setPixel(x + offX + offset, y + offY, color);
                    }
                }

            offset += font.getWidths()[unicode];
        }
    }

}
