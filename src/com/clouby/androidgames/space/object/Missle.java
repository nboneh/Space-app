package com.clouby.androidgames.space.object;

import com.badlogic.androidgames.framework.Pixmap;
import com.clouby.androidgames.space.Assets;

public class Missle extends WorldObject {
	enum MissleType{
		BASIC(300, 300, Assets.basicMissle, 1);

		private int speed;
		private int totalDistance;
		private Pixmap pixmap;
		private int numOfFrames;
		
		MissleType(int speed, int totalDistance, Pixmap pixmap, int numOfFrames){
			this.speed = speed;
			this.totalDistance = totalDistance;
			this.pixmap = pixmap; 
			this.numOfFrames = numOfFrames; 
		}

	}

	private int totalDistance;

	static Missle createMissle(MissleType missleType, int x,  int y,  float angle){
		float deltaX = (float) Math.sin(Math.toRadians(angle));
		float deltaY = - (float) Math.cos(Math.toRadians(angle));
		return new Missle(x, y, missleType.pixmap, deltaX,deltaY, missleType.speed, angle, missleType.totalDistance,
				missleType.numOfFrames);
	}

	static void recycleMissle(MissleType missleType, int x,  int y,  float angle, Missle missle){
		float deltaX = (float) Math.sin(Math.toRadians(angle));
		float deltaY = - (float) Math.cos(Math.toRadians(angle));
		missle.recycle(x, y, missleType.numOfFrames, missleType.pixmap, missleType.speed, deltaX, deltaY);
		missle.setAngle(angle);
	}

	private Missle(int x, int y, Pixmap pixmap, float deltaX, float deltaY, int speed,float angle, int totalDistance,
			int numOfFrames) {

		super(x, y, numOfFrames, pixmap, speed, deltaX, deltaY);
		this.angle = angle; 
		this.totalDistance = totalDistance;
	}

	@Override
	void update(float deltaTime){
		super.update(deltaTime);
		if(distanceTraveled >=totalDistance ){
			dead = true; 
		}
	}
}
