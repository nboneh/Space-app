package com.badlogic.androidgames.framework;


import android.graphics.Bitmap;

import com.badlogic.androidgames.framework.Graphics.PixmapFormat;

public interface Pixmap {
	
    public int getWidth();

    public int getHeight();

    public PixmapFormat getFormat();

    public void dispose();
    
    public Bitmap getPreRendered(String key); 
    
    public void insertPreRendered(String key, Bitmap rendered); 
}
