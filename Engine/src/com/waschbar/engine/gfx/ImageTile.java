package com.waschbar.engine.gfx;

public class ImageTile extends Image
{
    private int tileWidth, tileHeight;

    public ImageTile(String path, int width, int height)
    {
        super(path);
        this.tileWidth = width;
        this.tileHeight = height;
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
