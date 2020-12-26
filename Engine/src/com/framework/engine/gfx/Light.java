package com.framework.engine.gfx;

public class Light {

    public static final int NONE = 0;
    public static final int FULL = 1;

    private int radius, color;
    private int[] lm;

    public Light(int radius, int color) {
        this.radius = radius;
        this.color = color;
        this.lm = new int[4 * radius * radius];
        for (int y = 0; y < radius * 2; y++) {
            for (int x = 0; x < radius * 2; x++) {
                double distance = Math.sqrt((x - radius) * (x - radius) + (y - radius) * (y - radius));
                if (distance < radius) {
                    double ratio = 1.0 - distance / (double)radius;
                    lm[x + 2 * y * radius] = (((int)(((color >> 16) & 0xff) * ratio) << 16) | ((int)(((color >> 8) & 0xff) * ratio) << 8) | (int)((color & 0xff) * ratio));;
                } else {
                    lm[x + 2 * y * radius] = 0;
                }
            }
        }
    }

    public int getLightValue(int x, int y) {
        if (x < 0 || x >= radius * 2 || y < 0 || y >= radius * 2)
            return 0;
        return lm[x + y * radius * 2];
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int[] getLm() {
        return lm;
    }

    public void setLm(int[] lm) {
        this.lm = lm;
    }

}
