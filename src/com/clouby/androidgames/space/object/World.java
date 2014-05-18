package com.clouby.androidgames.space.object;

import java.util.List;


import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.clouby.androidgames.space.Assets;
import com.clouby.androidgames.space.Settings;
import com.clouby.androidgames.space.object.WorldObject.State;


public class World {

	enum WorldState{
		INTRO, 
		MAINMENU,
		HIGHSCORE,
		GAME;
	}

	private Spaceship playerSpaceship; 
	
	private WorldState nextState = null; 

	//Main Menu Items
	private WorldObject playButton;
	private WorldObject highscoreButton; 
	private WorldObject title;

	//High Score Items
	private WorldObject backButton; 

	private float transitionCounter;
	private WorldState worldState; 

	final static float TIC_TIME = 0.07f; 
	final static float TIME_DELAY_TRANSITION = 1f; 
	private static World inst;

	public static World getInst(){
		if(inst == null)
			inst = new World();
		return inst; 
	}


	private World(){
		playerSpaceship = new Spaceship(0,0,150);
		playerSpaceship.centerInX();
		playerSpaceship.centerInY();

		title = new Button(0, 20, Assets.title);
		title.centerInX();
		playButton = new Button(0,200,Assets.playButton);
		playButton.centerInX();
		highscoreButton = new Button(0, 250, Assets.highscoreButton);
		highscoreButton.centerInX();

		backButton = new Button(0,0 , Assets.backButton);
		backButton.setY(Settings.WORLD_HEIGHT - backButton.getHeight());
		setWorldState(WorldState.INTRO);
	}

	public void setWorldState(WorldState worldState) {
		this.worldState = worldState; 
		switch(worldState){
		case INTRO:
			playerSpaceship.setState(State.INTRO);
			break;
		case MAINMENU:
			title.setState(State.INTRO);
			playButton.setState(State.INTRO);
			highscoreButton.setState(State.INTRO);
			break;
		case GAME:
		case HIGHSCORE:
			backButton.setState(State.INTRO); 
			break;
		default:
			break;

		}
		transitionCounter = 0; 
	}

	public void update(float deltaTime, Input input){
		updatePlayerSpaceship(deltaTime,input); 
		if(transitionCounter >=TIME_DELAY_TRANSITION){
			switch(worldState){
			case INTRO:
				updateIntro(deltaTime);
				break;
			case MAINMENU:
				updateMainMenu(deltaTime); 
				break;
			case GAME:
			case HIGHSCORE:
				updateHighscore(deltaTime);
				break;
			default:
				break;

			}
		} 
		
		else
			transitionCounter += deltaTime; 
	}

	public void present(Graphics g){
		g.drawPixmap(Assets.background, 0, 0);

		switch(worldState){
		case GAME:
		case HIGHSCORE:
			presentHighscore( g);
			break;
		case MAINMENU:
			presentMainMenu(g); 
			break;
		default:
			break;

		}

		playerSpaceship.present(g);
	}

	private void updateIntro(float deltaTime){
		setWorldState(WorldState.MAINMENU); 
	}


	private void updateMainMenu(float deltaTime){
		title.update(deltaTime);
		playButton.update(deltaTime);
		highscoreButton.update(deltaTime);
		playerSpaceship.hitByMissles(playButton);
		playerSpaceship.hitByMissles(highscoreButton);
		if(nextState == null  && playButton.isDead()){
			highscoreButton.setState(State.DYING);
			title.setState(State.DYING);
			nextState = WorldState.GAME;
		}
		
		else if(nextState == null  && highscoreButton.isDead()){
			playButton.setState(State.DYING);
			title.setState(State.DYING);
			nextState = WorldState.HIGHSCORE;
		}
		
		if(nextState != null  && title.isDead()){
			setWorldState(nextState);
			nextState = null; 
		}
	}

	private void presentMainMenu(Graphics g){
		title.present(g);
		playButton.present(g);
		highscoreButton.present(g);
	}

	private void updateHighscore(float deltaTime){
		backButton.update(deltaTime);
		playerSpaceship.hitByMissles(backButton);
		
		if(nextState == null  && backButton.isDead()){
			nextState = WorldState.MAINMENU;
		}
		
		else if(nextState != null ){
			setWorldState(WorldState.MAINMENU);
			nextState = null;
		}
	}

	private void presentHighscore(Graphics g){
		backButton.present(g);
	}

	private void updatePlayerSpaceship(float deltaTime, Input input){
		if(worldState != WorldState.INTRO){
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



}
