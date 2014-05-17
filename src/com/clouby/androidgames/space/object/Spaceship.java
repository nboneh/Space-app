package com.clouby.androidgames.space.object;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.badlogic.androidgames.framework.Graphics;
import com.clouby.androidgames.space.Assets;
import com.clouby.androidgames.space.object.Missle.MissleType;

public class Spaceship extends WorldObject {

	private List<Missle> missles; 
	Spaceship(int x, int y, int speed) {
		super(x, y, 1, Assets.playerSpaceShip, speed,0 ,0);
		missles = new ArrayList<Missle>();
	}

	@Override
	public void setDeltas(float deltaX, float deltaY){
		if(state != State.INTRO && state != State.DYING)
			super.setDeltas(deltaX, deltaY);

	}
	@Override 
	void updateAngle(int refX, int refY){ 
		if(state != State.INTRO && state != State.DYING)
			super.updateAngle(refX, refY);
	}

	@Override
	void update(float deltaTime){
		switch(state){
		case INTRO:
			angle += deltaTime * 200;
			alpha += deltaTime * 40; 
			if(alpha >= MAX_ALPHA){
				alpha = MAX_ALPHA;
				state = State.NORMAL;
			}
			break; 
		case NORMAL:
			super.update(deltaTime);
			break;
		case ATTACKING:
			createMissle();
			state = State.NORMAL;
			break; 
		default:
			break; 
		}
		int numOfMissles = missles.size();
		for(int i = 0; i < numOfMissles; i++){
			missles.get(i).update(deltaTime);
		}
	}
	
	@Override
	void present(Graphics g){
		super.present(g);
		int numOfMissles = missles.size();
		for(int i = 0; i < numOfMissles; i++){
			missles.get(i).present(g);
		}
	}
	
	void playIntro(){
		state = State.INTRO;
		alpha = 0; 
	}
	void fire(){
		state = State.ATTACKING;
	}
	
	private void createMissle(){
		int hyp = height/2; 
		float midX = x + width/2; 
		float midY = y + hyp;
		int nostrilY = (int)(midY - hyp*Math.cos(Math.toRadians(angle)) -4);
		int nostrilX =  (int) (midX + hyp*Math.sin(Math.toRadians(angle)));
		
		int numOfMissles = missles.size();
		for(int i = 0; i < numOfMissles; i++){
			Missle missle = missles.get(i);
			if(missles.get(i).isDead()){
				Missle.recycleMissle(MissleType.BASIC, nostrilX, nostrilY, angle, missle);
				return;
			}
		
		}
		missles.add(Missle.createMissle(MissleType.BASIC, nostrilX, nostrilY, angle));
	}


}
