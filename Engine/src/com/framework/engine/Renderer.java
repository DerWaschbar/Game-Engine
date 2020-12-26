package com.framework.engine;

import com.framework.engine.gfx.*;
import com.framework.engine.gfx.Font;
import com.framework.engine.gfx.Image;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Renderer
{
    private ArrayList<ImageRequest> imageRequests = new ArrayList<ImageRequest>();
    private ArrayList<LightRequest> lightRequests = new ArrayList<LightRequest>();

    private int pW, pH; // PixelWidth, Pixel Height
    private int[] p;
    private int[] zBuffer;
    private int[] lightMap;
    private int[] lightBlock;

    private int zDepth = 0;
    private int ambientColor = -1;

    private boolean processing = false;
    private Font font = Font.STANDARD;
    private int camX, camY;

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

        Collections.sort(imageRequests, new Comparator<ImageRequest>() {
            @Override
            public int compare(ImageRequest o1, ImageRequest o2) {
                if(o1.zDepth < o2.zDepth)
                    return -1;
                if(o1.zDepth > o2.zDepth)
                    return 1;
                return 0;
            }
        });

        for(ImageRequest ir : imageRequests)
        {
            setzDepth(ir.zDepth);
            drawImage(ir.image, ir.offX, ir.offY);
        }

        for (int i = 0; i < lightRequests.size(); i++) {
            LightRequest lightRequest = lightRequests.get(i);
            drawLightRequest(lightRequest.light, lightRequest.locX, lightRequest.locY);
        }

        // Merge light and pixel maps
        for(int i = 0; i < p.length; i++)
        {
            float r = ((lightMap[i] >> 16) & 0xff) / 255f;
            float g = ((lightMap[i] >> 8) & 0xff) / 255f;
            float b = ((lightMap[i]) & 0xff) / 255f;

            p[i] = (((int)(((p[i] >> 16) & 0xff) * r) << 16) | ((int)(((p[i] >> 8) & 0xff) * g) << 8) | (int)(((p[i]) & 0xff) * b));
        }

        imageRequests.clear();
        lightRequests.clear();
        processing = false;
    }

    public void setPixel(int x, int y, int value)
    {
        int alpha = ((value >> 24) & 0xff);

        if((x < 0 || x >= pW || y < 0 || y >= pH) || alpha == 0)
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

    public void setLightBlock(int x, int y, int value)
    {
        if((x < 0 || x >= pW || y < 0 || y > pH))
            return;

        if(zBuffer[x + y * pW] > zDepth)
            return;

        lightBlock[x + y * pW] = value;
    }

    public void drawImage(Image image, int offsetX, int offsetY)
    {
        offsetX -= camX;
        offsetY -= camY;
        if(image.isAlpha() && !processing) {
            imageRequests.add(new ImageRequest(image, zDepth, offsetX, offsetY));
            return;
        }

        for(int x = Math.max(offsetX, 0); x < Math.min(image.getWidth()+offsetX, pW); x++)
            for(int y = Math.max(offsetY, 0); y < Math.min(image.getHeight()+offsetY, pH); y++)
            {
                setPixel(x, y, image.getP()[(x-offsetX) + (y-offsetY) * image.getWidth()]);
                setLightBlock(x, y, image.getLightBlock());
            }
    }

    public void drawImageTile(ImageTile image, int offsetX, int offsetY, int tileX, int tileY)
    {
        offsetX -= camX;
        offsetY -= camY;
        if(image.isAlpha() && !processing)
        {
            imageRequests.add(new ImageRequest(image.getTileImage(tileX, tileY), zDepth, offsetX, offsetY));
            return;
        }

        for(int x = Math.max(offsetX, 0); x < Math.min(image.getTileWidth()+offsetX, pW); x++)
            for(int y = Math.max(offsetY, 0); y < Math.min(image.getTileHeight()+offsetY, pH); y++)
            {
                setPixel(x, y, image.getP()[(x-offsetX+tileX*image.getTileWidth()) + (y-offsetY+tileY*image.getTileHeight()) * image.getWidth()]);
                setLightBlock(x, y, image.getLightBlock());
            }
    }

    public void drawText(String text, int offX, int offY, int color)
    {
        offX -= camX;
        offY -= camY;
        Image fontImg = font.getFontImage();
        int offset = 0;

        for(int i = 0; i < text.length(); i++)
        {
            int unicode = text.codePointAt(i);

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
        offX -= camX;
        offY -= camY;
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
        offX -= camX;
        offY -= camY;

        for(int x = 0; x < width; x++)
            for(int y = 0; y < height; y++)
                setPixel(x + offX, y + offY, color);
    }

    public void drawLight(Light light, int offX, int offY) {
        lightRequests.add(new LightRequest(light, offX, offY));
    }

    private void drawLightRequest(Light light, int offX, int offY) {
        offX -= camX;
        offY -= camY;
        for (int i = 0; i <= light.getRadius() * 2; i++) {
            drawLightLine(light, light.getRadius(), light.getRadius(), i, 0, offX, offY);
            drawLightLine(light, light.getRadius(), light.getRadius(), i, light.getRadius() * 2, offX, offY);
            drawLightLine(light, light.getRadius(), light.getRadius(), 0, i, offX, offY);
            drawLightLine(light, light.getRadius(), light.getRadius(), light.getRadius() * 2, i, offX, offY);
        }
    }

    public void drawLightLine(Light light, int x0, int y0, int x1, int y1, int offX, int offY) {
        int dx = Math.abs(x0 - x1);
        int dy = Math.abs(y0 - y1);
        int sx = -1;
        int sy = -1;
        if (x0 < x1)
            sx = 1;
        if (y0 < y1)
            sy = 1;

        int error = dx - dy;
        int error2;

        while(true) {

            int screenX = x0 - light.getRadius() + offX;
            int screenY = y0 - light.getRadius() + offY;

            if (screenX < 0 || screenY < 0 || screenX >= pW || screenY >= pH)
                return;

            int lightColor = light.getLightValue(x0, y0);
            if (lightColor == 0)
                return;

            if (lightBlock[screenX + screenY * pW] == Light.FULL)
                return;

            setLightMap(screenX, screenY, lightColor);

            if (x0 == x1 && y0 == y1)
                break;

            error2 = error * 2;
            if (error2 > -dy) {
                error -= dy;
                x0 += sx;
            }
            if (error2 < dx) {
                error += dx;
                y0 += sy;
            }
        }
    }

    public int getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(int ambientColor) {
        this.ambientColor = ambientColor;
    }

    public int getzDepth()
    {
        return zDepth;
    }

    public void setzDepth(int zDepth)
    {
        this.zDepth = zDepth;
    }

    public int getCamX() {
        return camX;
    }

    public void setCamX(int camX) {
        this.camX = camX;
    }

    public int getCamY() {
        return camY;
    }

    public void setCamY(int camY) {
        this.camY = camY;
    }
}
