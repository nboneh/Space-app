package com.badlogic.androidgames.framework.impl;

import java.util.List;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;

import com.badlogic.androidgames.framework.Input;

public class AndroidInput implements Input {    
	TouchHandler touchHandler;
	AccelerometerHandler accelHandler; 

	public AndroidInput(Context context, View view, float scaleX, float scaleY) {
		accelHandler = new AccelerometerHandler(context);     
		touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
	}
	
	
	@Override
	public float getAccelX() {
		return accelHandler.getAccelX();
	}

	@Override
	public float getAccelY() {
		return accelHandler.getAccelY();
	}

	@Override
	public float getAccelZ() {
		return accelHandler.getAccelZ();
	}


	@Override
	public boolean isTouchDown(int pointer) {
		return touchHandler.isTouchDown(pointer);
	}

	@Override
	public int getTouchX(int pointer) {
		return touchHandler.getTouchX(pointer);
	}

	@Override
	public int getTouchY(int pointer) {
		return touchHandler.getTouchY(pointer);
	}


	@Override
	public List<TouchEvent> getTouchEvents() {
		return touchHandler.getTouchEvents();
	}



}
