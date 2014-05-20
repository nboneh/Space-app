package com.clouby.androidgames.space.object;

import com.badlogic.androidgames.framework.Pixmap;
import com.clouby.androidgames.space.Assets;

public class Missle extends WorldObject {

	private int totalDistance;

	Missle(int x, int y, Pixmap pixmap, float angle) {

		super(x, y, pixmap, 300,1, 1, 3, 1); 
		float deltaX = (float) Math.sin(Math.toRadians(angle));
		float deltaY = - (float) Math.cos(Math.toRadians(angle));
		speed = 300; 
		totalDistance = 250; 
		this.angle = angle;
		setDeltas(deltaX, deltaY);
		volume = 0; 
	}

	void reuse(int x, int y, float angle){
		float deltaX = (float) Math.sin(Math.toRadians(angle));
		float deltaY = - (float) Math.cos(Math.toRadians(angle));
		setState(State.NORMAL);
		this.x = x;
		this.y = y; 
		speed = 300; 
		distanceTraveled = 0;
		this.angle = angle;
		setDeltas(deltaX, deltaY);
		dead = false;
	}

	@Override
	void update(float deltaTime){
		super.update(deltaTime);
		if(distanceTraveled >=totalDistance ){
			setState(State.DYING);
			speed = 0; 
		}
	}

	@Override
	boolean hit(WorldObject object){
		if(super.hit(object)){
			setState(State.DYING);
			dead = true; 
			return true;
		} return false; 
	}
}
