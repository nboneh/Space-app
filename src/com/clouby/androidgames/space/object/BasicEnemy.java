package com.clouby.androidgames.space.object;

import com.clouby.androidgames.space.Assets;

public class BasicEnemy extends WorldObject {

	private int score; 

	BasicEnemy( int speed, float size, int lives) {
		super(0, 0, Assets.basicEnemy, speed, 1, 1, 5, size);
		nextSound = null; 
		setState(State.NORMAL);
		score = (int)( (size + speed/50.0));
		this.lives = lives; 
	}

	void recycle( int speed, float size, int lives){
		super.init(0, 0, Assets.basicEnemy, speed, 1, 1, 5, size);
		nextSound = null;
		setState(State.NORMAL);
		this.size = size; 
		score = (int)((size + speed/50.0));
		this.lives = lives;
		dead = false;
		
	}

	@Override
	void setState(State state){
		super.setState(state);
		if(state == State.DYING)
			World.getInst().incScore(score);

	}

}
