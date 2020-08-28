package com.waschbar.engine;

import com.waschbar.engine.gfx.Image;
import com.waschbar.engine.gfx.ImageRequest;
import com.waschbar.engine.gfx.ImageTile;
import com.waschbar.engine.gfx.Font;

import java.awt.*;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Renderer
{
    private int pW, pH; // PixelWidth, Pixel Height
    private int[] p;
    private int[] zBuffer;
    private int[] lightMap;
    private int[] lightBlock;

    private int zDepth = 0;
    private int ambientColor = 0xff6b6b6b;

    private boolean processing = false;
    private ArrayList<ImageRequest> requests = new ArrayList<ImageRequest>();
    private Font font = Font.STANDARD;

    public Renderer(GameContainer gc)
    {
        pW = gc.getWidth();
        pH = gc.getHeight();
        p = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
        zBuffer = new int[p.length];
        lightMap = new int[p.length];
        lightBlock = new int[p.length];
    }

    public void clear()
    {
        for(int i = 0; i < p.length; i++)
        {
            p[i] = 0;
            zBuffer[i] = 0;
            lightMap[i] = ambientColor;
            lightBlock[i] = 0;
        }
    }

    public void process()
    {
        processing = true;

        Collections.sort(requests, new Comparator<ImageRequest>() {
            @Override
            public int compare(ImageRequest o1, ImageRequest o2) {
                if(o1.zDepth < o2.zDepth)
                    return -1;
                if(o1.zDepth > o2.zDepth)
                    return 1;
                return 0;
            }
        });

        for(ImageRequest ir : requests)
        {
            setzDepth(ir.zDepth);
            drawImage(ir.image, ir.offX, ir.offY);
        }

        // Merge light and pixel maps
        for(int i = 0; i < p.length; i++)
        {
            float r = ((lightMap[i] >> 16) & 0xff) / 255f;
            float g = ((lightMap[i] >> 8) & 0xff) / 255f;
            float b = ((lightMap[i]) & 0xff) / 255f;

            p[i] = (((int)(((p[i] >> 16) & 0xff) * r) << 16) | ((int)(((p[i] >> 8) & 0xff) * g) << 8) | (int)(((p[i]) & 0xff) * b));
        }

        requests.clear();
        processing = false;
    }

    public void setPixel(int x, int y, int value)
    {
        int alpha = ((value >> 24) & 0xff);
        if((x < 0 || x >= pW || y < 0 || y > pH) || alpha == 0)
            return;

        int index = x + y * pW;
        if(zBuffer[index] > zDepth)
            return;

        zBuffer[index] = zDepth;

        if(alpha == 255)
        {
            p[index] = value;
        }
        else
        {
            int pC = p[index];
            int r = ((pC >> 16) & 0xff) - (int)((((pC >> 16) & 0xff) - ((value >> 16) & 0xff)) * (alpha / 255f));
            int g = ((pC >> 8) & 0xff) - (int)((((pC >> 8) & 0xff) - ((value >> 8) & 0xff)) * (alpha / 255f));
            int b = ((pC) & 0xff) - (int)((((pC) & 0xff) - ((value) & 0xff)) * (alpha / 255f));

            p[index] = (r << 16 | g << 8 | b);
        }

    }

    public void setLightMap(int x, int y, int value)
    {
        if((x < 0 || x >= pW || y < 0 || y > pH))
            return;

        int index = x + y * pW;

        int baseColor = lightMap[index];

        int maxRed = Math.max(((baseColor >> 16) & 0xff), ((value >> 16) & 0xff));
        int maxGreen = Math.max(((baseColor >> 8) & 0xff), ((value >> 8) & 0xff));
        int maxBlue = Math.max(((baseColor) & 0xff), ((value) & 0xff));

        lightMap[index] = (maxRed << 16 | maxGreen << 8 | maxBlue);
    }

    public void drawImage(Image image, int offsetX, int offsetY)
    {
        if(image.isAlpha() && !processing)
        {
            requests.add(new ImageRequest(image, zDepth, offsetX, offsetY));
            return;
        }

        for(int x = Math.max(offsetX, 0); x < Math.min(image.getWidth()+offsetX, pW); x++)
            for(int y = Math.max(offsetY, 0); y < Math.min(image.getHeight()+offsetY, pH); y++)
            {
                setPixel(x, y, image.getP()[(x-offsetX) + (y-offsetY) * image.getWidth()]);
            }
    }

    public void drawImageTile(ImageTile image, int offsetX, int offsetY, int tileX, int tileY)
    {
        if(image.isAlpha() && !processing)
        {
            requests.add(new ImageRequest(image.getTileImage(tileX, tileY), zDepth, offsetX, offsetY));
            return;
        }

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

    // TODO: Do not loop out-of-border pixels

    public void drawRect(int offX, int offY, int width, int height, int color)
    {
        for(int x = 0; x <= width; x++)
        {
            setPixel(x+offX, offY, color);
            setPixel(x+offX, offY+height, color);
        }

        for(int y = 0; y <= height; y++)
        {
            setPixel(offX, y+offY, color);
            setPixel(offX+width, y+offY, color);
        }
    }

    public void drawFillRect(int offX, int offY, int width, int height, int color)
    {
        for(int x = 0; x <= width; x++)
            for(int y = 0; y <= height; y++)
                setPixel(x+offX, y+offY, color);
    }

    public int getzDepth()
    {
        return zDepth;
    }

    public void setzDepth(int zDepth)
    {
        this.zDepth = zDepth;
    }
}
