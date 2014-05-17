package com.badlogic.androidgames.framework;

import android.graphics.Typeface;

public interface Graphics {
    public static enum PixmapFormat {
        ARGB8888, ARGB4444, RGB565
    }

    public Pixmap newPixmap(String fileName, PixmapFormat format);

    public void clear(int color);

    public void drawPixel(int x, int y, int color);

    public void drawLine(int x, int y, int x2, int y2, int color);

    public void drawRect(int x, int y, int width, int height, int color);

    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
            int srcWidth, int srcHeight);
   
    public void drawPixmap(Pixmap pixmap, int x, int y);
    
    public Typeface newTypeFace (String fileName);
    
    public void drawFont(int x, int y, int size, String message, int color ,Typeface font);

    public int getWidth();

    public int getHeight();

	public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
			int width, int height, int angle, int alpha);
}
