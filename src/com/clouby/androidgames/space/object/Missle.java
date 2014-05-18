package com.clouby.androidgames.space.object;

import com.badlogic.androidgames.framework.Pixmap;
import com.clouby.androidgames.space.Assets;

public class Missle extends WorldObject {
	enum MissleType{
		BASIC(300, 300, Assets.basicMissle, 0,0, 1);

		private int speed;
		private int totalDistance;
		private Pixmap pixmap;
		private int numOfFrames;
		private int attackFrame;
		private int dyingFrame; 

		MissleType(int speed, int totalDistance, Pixmap pixmap, int attackFrame, int dyingFrame, int numOfFrames){
			this.speed = speed;
			this.totalDistance = totalDistance;
			this.pixmap = pixmap; 
			this.numOfFrames = numOfFrames; 
			this.attackFrame = attackFrame;
			this.dyingFrame = dyingFrame; 
		}

	}

	private int totalDistance;

	static Missle createMissle(MissleType missleType, int x,  int y,  float angle){
		float deltaX = (float) Math.sin(Math.toRadians(angle));
		float deltaY = - (float) Math.cos(Math.toRadians(angle));
		return new Missle(x, y, missleType.pixmap, deltaX,deltaY, missleType.speed, angle, missleType.totalDistance,
				missleType.attackFrame, missleType.dyingFrame,missleType.numOfFrames);
	}

	static void recycleMissle(MissleType missleType, int x,  int y,  float angle, Missle missle){
		float deltaX = (float) Math.sin(Math.toRadians(angle));
		float deltaY = - (float) Math.cos(Math.toRadians(angle));
		missle.recycle(x, y,  missleType.pixmap, missleType.speed, 
				missleType.attackFrame, missleType.dyingFrame,missleType.numOfFrames);
		missle.setAngle(angle);
		missle.setDeltas(deltaX, deltaY);
		missle.totalDistance = missleType.totalDistance;
	}

	private Missle(int x, int y, Pixmap pixmap, float deltaX, float deltaY, int speed,float angle, int totalDistance,
			int attackFrame, int dyingFrame, int numOfFrames) {

		super(x, y, pixmap, speed,attackFrame, dyingFrame, numOfFrames); 
		this.angle = angle; 
		this.totalDistance = totalDistance;
		setDeltas(deltaX, deltaY);
	}

	@Override
	void update(float deltaTime){
		super.update(deltaTime);
		if(distanceTraveled >=totalDistance ){
			dead =true;
		}
	}

	@Override
	boolean hit(WorldObject object){
		if(super.hit(object)){
			dead = true; 
			return true;
		} return false; 
	}
}
