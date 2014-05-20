package com.clouby.androidgames.space.object;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.androidgames.framework.Graphics;
import com.clouby.androidgames.space.Assets;

public class Spaceship extends WorldObject {

	private List< Missle> missles; 
	private int shootingFrame = 3;
	Spaceship(int x, int y, int speed) {
		super(x, y, Assets.playerSpaceShip, speed,1,4,7, 1);
		missles = new ArrayList<Missle>();
	}

	@Override
	void update(float deltaTime){
		super.update(deltaTime);
		if(tic && frame == shootingFrame)
			createMissle();
		int numOfMissles = missles.size();
		for(int i = 0; i < numOfMissles; i++){
			missles.get(i).update(deltaTime);
			if(dead)
				missles.get(i).setState(State.DYING);
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
		Assets.basicMissleFire.play(volume);
		int hyp = height/2; 
		float midX = x + width/2; 
		float midY = y + hyp;
		int nostrilY = (int)(midY - hyp*Math.cos(Math.toRadians(angle)) -4);
		int nostrilX =  (int) (midX + hyp*Math.sin(Math.toRadians(angle)));

		int numOfMissles = missles.size();
		for(int i = 0; i < numOfMissles; i++){
			Missle missle = missles.get(i);
			if(missle.isDead()){
				missle.reuse(nostrilX,nostrilY, angle);
				return;
			}

		}
		missles.add(new Missle(nostrilX, nostrilY, Assets.basicPlayerMissle, angle));
	}


}
