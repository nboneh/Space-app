package com.clouby.androidgames.space.object;

import java.util.List;


import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.clouby.androidgames.space.Assets;
import com.clouby.androidgames.space.Settings;
import com.clouby.androidgames.space.object.WorldObject.State;


public class World {

	private Spaceship playerSpaceship; 
	private WorldObject playButton;
	private WorldObject highscoreButton; 
	private WorldObject backButton; 
	private int titleX; 

	final static float TIC_TIME = 0.05f; 
	private static World inst;

	public static World getInst(){
		if(inst == null)
			inst = new World();
		return inst; 
	}

	private World(){
		playerSpaceship = new Spaceship(0,100,150);
		playerSpaceship.centerInX();

		playButton = new Button(0,200,Assets.playButton);
		playButton.centerInX();
		
		highscoreButton = new Button(0, 250, Assets.highscoreButton);
		highscoreButton.centerInX();
	
		titleX = Settings.WORLD_WIDTH/2 - Assets.title.getWidth()/2; 

		backButton = new Button(0,0 , Assets.backButton);
		
	}

	public void updateMainScreen(float deltaTime, Input input){
		updatePlayerSpaceship(deltaTime, input);
		playButton.update(deltaTime);
		highscoreButton.update(deltaTime);
		playerSpaceship.hitByMissles(playButton);
		playerSpaceship.hitByMissles(highscoreButton);
		playerSpaceship.hitByMissles(backButton);
	}

	public void presentMainScreen(Graphics g){
		g.drawPixmap(Assets.background, 0, 0);
		g.drawPixmap(Assets.title, titleX,10);
		playButton.present(g);
		highscoreButton.present(g);
		playerSpaceship.present(g);
	}
	
	public void updateHighscoreScreen(float deltaTime, Input input){
		updatePlayerSpaceship(deltaTime, input);
		backButton.update(deltaTime);
		
	}
	
	public void presentHighscoreScreen(Graphics g){
		backButton.present(g);
	}

	private void updatePlayerSpaceship(float deltaTime, Input input){
		//Inputs should be flipped 
		float deltaX =input.getAccelY();
		//Minus so you can hold the phone tilted
		float deltaY = input.getAccelX() - 3;
		float hyp = (float) Math.sqrt(deltaX*deltaX + deltaY*deltaY); 
		if(hyp > 1){
			//Cap speed at 2 radii
			deltaX /= (hyp /2);
			deltaY /= (hyp /2); 
		}

		playerSpaceship.setDeltas(deltaX, deltaY);

		List<TouchEvent> touchEvents = input.getTouchEvents();
		int size = touchEvents.size();
		for(int i = 0; i < size; i++){
			TouchEvent event = touchEvents.get(i);
			switch(event.type){
			case TouchEvent.TOUCH_DOWN:
				playerSpaceship.setState(State.ATTACKING);
			case TouchEvent.TOUCH_DRAGGED:
				playerSpaceship.updateAngle(event.x, event.y);
				break; 
			}
		}

		playerSpaceship.update(deltaTime);

	}



}
