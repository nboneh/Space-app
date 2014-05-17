package com.clouby.androidgames.space.object;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.androidgames.framework.Graphics;
import com.clouby.androidgames.space.Assets;
import com.clouby.androidgames.space.object.Missle.MissleType;

public class Spaceship extends WorldObject {

	private List< Missle> missles; 
	private int shootingFrame = 3;
	Spaceship(int x, int y, int speed) {
		super(x, y, Assets.playerSpaceShip, speed,1,4,4);
		missles = new ArrayList<Missle>();
	}

	@Override
	void update(float deltaTime){
		super.update(deltaTime);
		switch(state){
		case NORMAL:
			break;
		case ATTACKING:
			if(tic && frame == shootingFrame){
			createMissle();
			state = State.NORMAL;
			}
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
	
	@Override 
	boolean hit(WorldObject object){
		if(hitByBody(object))
			return true;
		return hitByMissles(object);
	}
	
	boolean hitByMissles(WorldObject object){
		int numOfMissles = missles.size();
		for(int i = 0; i < numOfMissles; i++){
			WorldObject missle = missles.get(i);
			if( missle.hit(object)){
				return true ;
			}
		}
		return false; 
	}
	
	boolean hitByBody(WorldObject object){
		return super.hit(object);
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
			if(missle.isDead()){
				Missle.recycleMissle(MissleType.BASIC, nostrilX, nostrilY, angle, missle);
				return;
			}
		
		}
		missles.add(Missle.createMissle(MissleType.BASIC, nostrilX, nostrilY, angle));
	}


}
