package com.clouby.androidgames.space.object;

import java.util.List;


import android.graphics.Color;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.clouby.androidgames.space.Assets;
import com.clouby.androidgames.space.Settings;
import com.clouby.androidgames.space.object.WorldObject.State;


public class World {

	public static enum WorldState{
		INTRO, 
		MAINMENU,
		GAMEOVER,
		HIGHSCORE,
		GAME;
	}

	private Spaceship playerSpaceship; 

	private WorldState nextState = null; 

	//Main Menu Items
	private WorldObject playButton;
	private WorldObject highscoreButton; 
	private WorldObject title;
	private WorldObject phoneOrientation; 

	//High Score Items
	private WorldObject highScoreTitle;
	private WorldObject onlineTitle;
	private WorldObject localTitle;
	private WorldObject backButton; 


	//Enemy Generator
	private EnemyGenerator enemyGenerator; 
	private float transitionCounter;
	private WorldState worldState; 
	
	//GameOver items
	private WorldObject gameOverTitle;
	private WorldObject newGameButton;
	private WorldObject mainMenuButton; 

	final static float TIC_TIME = 0.07f; 
	final static float TIME_DELAY_TRANSITION = 1f; 
	private int score = 0; 
	private int scoreMultiplier = 1; 

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
		phoneOrientation = new Button(0, 0, Assets.phoneOrientation);
		phoneOrientation.setY(Settings.WORLD_HEIGHT - phoneOrientation.getHeight());


		highScoreTitle = new Button(0,20, Assets.highscoreTitle);
		highScoreTitle.setSize(1.5f);
		highScoreTitle.centerInX();

		localTitle = new Button(30, 70, Assets.localTitle);
		onlineTitle = new Button(290, 70, Assets.onlineTitle);
		backButton = new Button(0,0 , Assets.backButton);
		backButton.setY(Settings.WORLD_HEIGHT - backButton.getHeight());

		gameOverTitle = new Button(0, 20, Assets.gameOver);
		gameOverTitle.setSize(1.5f);
		gameOverTitle.centerInX();
		newGameButton = new Button(0, 170, Assets.newGame);
		newGameButton.centerInX();
		mainMenuButton = new Button(0, 210, Assets.mainMenu );
		mainMenuButton.centerInX();

		enemyGenerator = new EnemyGenerator(playerSpaceship);

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
			phoneOrientation.setState(State.INTRO);
			break;
		case GAME:
			score = 0;
			setScoreMultiplier(1);
			enemyGenerator.reset();
			break; 
		case GAMEOVER:
			gameOverTitle.setState(State.INTRO);
			newGameButton.setState(State.INTRO);
			mainMenuButton.setState(State.INTRO);
			highscoreButton.setState(State.INTRO);
			playerSpaceship.setState(State.INTRO);
			
			break;
		case HIGHSCORE:
			highScoreTitle.setState(State.INTRO);
			onlineTitle.setState(State.INTRO);
			localTitle.setState(State.INTRO);
			backButton.setState(State.INTRO); 
			break;
		default:
			break;

		}
		transitionCounter = 0; 
	}

	public void update(float deltaTime, Input input){
		updateInput(deltaTime,input); 
		if(transitionCounter >=TIME_DELAY_TRANSITION){
			switch(worldState){
			case INTRO:
				updateIntro(deltaTime);
				break;
			case MAINMENU:
				updateMainMenu(deltaTime); 
				break;
			case GAME:
				updateGame(deltaTime);
				break;
			case GAMEOVER:
				updateGameOver(deltaTime);
				break; 
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
			presentGame(g);
			break; 
		case GAMEOVER:
			presentGameOver(g);
			break; 
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
		phoneOrientation.update(deltaTime);
		if(nextState == null  && playButton.isDead()){
			highscoreButton.setState(State.DYING);
			title.setState(State.DYING);
			phoneOrientation.setState(State.DYING);
			nextState = WorldState.GAME;
		}

		else if(nextState == null  && highscoreButton.isDead()){
			playButton.setState(State.DYING);
			title.setState(State.DYING);
			phoneOrientation.setState(State.DYING);
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
		phoneOrientation.present(g);
	}

	private void updateHighscore(float deltaTime){
		highScoreTitle.update(deltaTime);
		onlineTitle.update(deltaTime);
		localTitle.update(deltaTime);
		backButton.update(deltaTime);

		if(nextState == null  && backButton.isDead()){
			highScoreTitle.setState(State.DYING);
			onlineTitle.setState(State.DYING);
			localTitle.setState(State.DYING);
			nextState = WorldState.MAINMENU;
		}

		else if(nextState != null && highScoreTitle.isDead()){
			setWorldState(WorldState.MAINMENU);
			nextState = null;
		}
	}

	private void presentHighscore(Graphics g){
		highScoreTitle.present(g);
		onlineTitle.present(g);
		localTitle.present(g);
		backButton.present(g);
		int len = Settings.size;
		if(highScoreTitle.getState() == State.NORMAL){
			for(int i = 0; i < len; i++){
				g.drawFont(30, i *35 + 125, 18, (i+1)  + ". " + Settings.localHighscores[i].getName(), Color.RED, Assets.font);
				g.drawFont(140, i *35 + 125, 18,  Settings.localHighscores[i].getScore() + "", Color.RED, Assets.font);
		}
		}
	}

	private void updateGame(float deltaTime){
		enemyGenerator.update(deltaTime);
		if(playerSpaceship.isDead()){
			setScoreMultiplier(0);
			enemyGenerator.destroy();
			if(enemyGenerator.isDead()){
				setWorldState(WorldState.GAMEOVER);
			}
		}

	}

	private void presentGame(Graphics g){
		enemyGenerator.present(g);
		g.drawFont(10, 20, 20, "Score: " + score, Color.RED, Assets.font);
	}
	
	private void updateGameOver(float deltaTime){
		gameOverTitle.update(deltaTime);
		newGameButton.update(deltaTime);
		mainMenuButton.update(deltaTime);
		highscoreButton.update(deltaTime);
		

		if(nextState == null  && highscoreButton.isDead()){
			newGameButton.setState(State.DYING);
			gameOverTitle.setState(State.DYING);
			mainMenuButton.setState(State.DYING);
			nextState = WorldState.HIGHSCORE;
		}
		
		else if(nextState == null  && mainMenuButton.isDead()){
			newGameButton.setState(State.DYING);
			gameOverTitle.setState(State.DYING);
			highscoreButton.setState(State.DYING);
			nextState = WorldState.MAINMENU;
		}
		
		else if(nextState == null  && newGameButton.isDead()){
			highscoreButton.setState(State.DYING);
			gameOverTitle.setState(State.DYING);
			mainMenuButton.setState(State.DYING);
			nextState = WorldState.GAME;
		}

		if(nextState != null  && gameOverTitle.isDead()){
			setWorldState(nextState);
			nextState = null; 
		}
	}
	
	private void presentGameOver(Graphics g){
		highscoreButton.present(g);
		gameOverTitle.present(g);
		mainMenuButton.present(g);
		newGameButton.present(g);
		if(gameOverTitle.getState() == State.NORMAL)
			g.drawFont(160, 140, 40, "Score: " + score, Color.RED, Assets.font);
	}

	private void updateInput(float deltaTime, Input input){
		if(playerSpaceship.state != State.DYING){

			List<TouchEvent> touchEvents = input.getTouchEvents();
			int size = touchEvents.size();
			for(int i = 0; i < size; i++){
				TouchEvent event = touchEvents.get(i);
				switch(event.type){
				case TouchEvent.TOUCH_DOWN:
					switch(worldState){
					case GAME:
						break;
					case HIGHSCORE:
						if(backButton.clicked(event.x, event.y)){
							backButton.setState(State.DYING);
							return;
						}
						break;
					case INTRO:
						return; 
					case GAMEOVER:
						 if(highscoreButton.clicked(event.x, event.y)){
							highscoreButton.setState(State.DYING);
							return; 
						}
						 else if(mainMenuButton.clicked(event.x, event.y)){
							 	mainMenuButton.setState(State.DYING);
								return; 
							}
						 else if(newGameButton.clicked(event.x, event.y)){
							 newGameButton.setState(State.DYING);
								return; 
							}
						 break;
					case MAINMENU:
						if(playButton.clicked(event.x, event.y)){
							playButton.setState(State.DYING);
							return;
						}
						else if(highscoreButton.clicked(event.x, event.y)){
							highscoreButton.setState(State.DYING);
							return; 
						}
						else if(phoneOrientation.clicked(event.x, event.y)){
							phoneOrientation.setState(State.INTRO);
							Settings.defaultY = input.getAccelY();
							Settings.defaultX = input.getAccelX();
							return;
						}
						break;
					default:
						break;
					}
					playerSpaceship.setState(State.ATTACKING);
				case TouchEvent.TOUCH_DRAGGED:
					playerSpaceship.updateAngle(event.x, event.y);
					break; 
				}
			}
		}
		float deltaX =input.getAccelY() - Settings.defaultY;
		float deltaY = input.getAccelX() - Settings.defaultX;
		float hyp = (float) Math.sqrt(deltaX*deltaX + deltaY*deltaY); 
		if(hyp > 1){
			//Cap speed at 2 radii
			deltaX /= (hyp /2);
			deltaY /= (hyp /2); 
		}

		playerSpaceship.setDeltas(deltaX, deltaY);

		playerSpaceship.update(deltaTime);
	}



	void incScore(int score) {
		this.score += score * scoreMultiplier; 
	}
	void setScoreMultiplier(int scoreMultiplier){
		this.scoreMultiplier = scoreMultiplier;
	}
	
	public WorldState getWorldState(){
		return worldState;
	}



}
