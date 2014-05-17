package com.badlogic.androidgames.framework.impl;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

import com.badlogic.androidgames.framework.Graphics.PixmapFormat;
import com.badlogic.androidgames.framework.Pixmap;

public class AndroidPixmap implements Pixmap {
    Bitmap bitmap;
    PixmapFormat format;
    Map<String, Bitmap> preRenderedMap;
    
    public AndroidPixmap(Bitmap bitmap, PixmapFormat format) {
        this.bitmap = bitmap;
        this.format = format;
        preRenderedMap = new HashMap<String, Bitmap>(); 
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public PixmapFormat getFormat() {
        return format;
    }

    @Override
    public void dispose() {
        bitmap.recycle();
    }


	@Override
	public void insertPreRendered(String key, Bitmap rendered) {
		preRenderedMap.put(key, rendered);
	}

	@Override
	public Bitmap getPreRendered(String key) {
		return preRenderedMap.get(key);
	}      
}
