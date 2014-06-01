package com.clouby.androidgames.space.object;

import com.clouby.androidgames.space.Assets;

public class BasicEnemy extends WorldObject {

	private int score; 

	BasicEnemy( float size,  int lives, int speed,  int angle) {
		super(0, 0, Assets.basicEnemy, speed, 1, 1, 5, size);
		nextSound = null; 
		setState(State.NORMAL);
		speed = (int) ((1/size ) * 100);
		this.angle = angle; 
		this.lives = lives;
	}

	void recycle( float size, int lives, int speed, int angle){
		super.init(0, 0, Assets.basicEnemy, speed, 1, 1, 5, size);
		nextSound = null;
		setState(State.NORMAL);
		speed = (int) ((1/size ) * 100);
		this.size = size; 
		this.angle =angle; 
		this.lives = lives; 
		dead = false;
		
	}

	@Override
	void setState(State state){
		super.setState(state);
		if(state == State.DYING){
			speed /= 2;
			if( lives <= 0)
				World.getInst().incScore(1);
		}

	}

}
