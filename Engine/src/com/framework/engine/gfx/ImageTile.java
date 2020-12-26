package com.framework.engine.gfx;

public class ImageTile extends Image
{
    private int tileWidth, tileHeight;

    public ImageTile(String path, int width, int height)
    {
        super(path);
        this.tileWidth = width;
        this.tileHeight = height;
    }

    public Image getTileImage(int tileX, int tileY)
    {
        int[] p = new int[tileWidth * tileHeight];

        for(int x = 0; x < tileWidth; x++)
            for(int y = 0; y < tileHeight; y++)
                p[x + y *tileWidth] = this.getP()[(x + tileX * tileWidth) + (y + tileY * tileHeight) * this.getWidth()];

        return new Image(p, tileWidth, tileHeight);
    }

    public int getTileWidth()
    {
        return tileWidth;
    }

    public void setTileWidth(int tileWidth)
    {
        this.tileWidth = tileWidth;
    }

    public int getTileHeight()
    {
        return tileHeight;
    }

    public void setTileHeight(int tileHeight)
    {
        this.tileHeight = tileHeight;
    }
}
