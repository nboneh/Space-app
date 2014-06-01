package com.clouby.androidgames.space.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.androidgames.framework.Graphics;
import com.clouby.androidgames.space.Settings;
import com.clouby.androidgames.space.object.WorldObject.State;

public class EnemyGenerator {
	private List<BasicEnemy> enemies; 
	private Random rand;
	private Spaceship playerSpaceship; 
	private float ticsToSpawn = 1.2f;
	private  float ticCounter = .5f;
	private float spawnMultiplier = .99f; 


	EnemyGenerator(Spaceship playerSpaceship){
		enemies = new ArrayList<BasicEnemy>();
		rand = new Random(); 
		this.playerSpaceship = playerSpaceship; 
	}

	void generate(){

		float size = (rand.nextFloat()  + .5f);
		int speed = rand.nextInt(200) + 50;
		int lives = 1;
		int angle = rand.nextInt(360);

		int numOfEnemies = enemies.size();
		BasicEnemy enemy = null;
		for(int i = 0; i < numOfEnemies; i++){
			BasicEnemy cycleEnemy = enemies.get(i);
			if(cycleEnemy.isDead()){
				enemy = cycleEnemy; 
				enemy.recycle(size,  lives, speed, angle);
				break; 
			}

		}
		if(enemy == null){
			enemy = new BasicEnemy( size, lives, speed, angle);
			enemies.add(enemy);
		}

		int x =0;
		int y =0 ; 
		float deltaX = rand.nextFloat();
		float deltaY = (float) Math.sqrt(1 - deltaX * deltaX); 

		int generateRegion = rand.nextInt(4);
		switch(generateRegion){
		case 0:
			x = rand.nextInt(Settings.WORLD_WIDTH);
			y = enemy.getTopBoundary();
			if(x > Settings.WORLD_WIDTH/2)
				deltaX = - deltaX; 
			break; 
		case 1:
			x = enemy.getRightBoundary();
			y = rand.nextInt(Settings.WORLD_HEIGHT);
			if(y > Settings.WORLD_HEIGHT/2)
				deltaY = -deltaY;
			break;
		case 2:
			x = rand.nextInt(Settings.WORLD_WIDTH);
			y = enemy.getBottomBoundary();
			if(x > Settings.WORLD_WIDTH/2)
				deltaX = - deltaX; 
			deltaY = - deltaY;
			break;
		case 3:
			x = enemy.getLeftBoundary();
			y = rand.nextInt(Settings.WORLD_HEIGHT);
			if(y > Settings.WORLD_HEIGHT/2)
				deltaY = -deltaY;
			deltaX = -deltaX;
			break; 
		}
		enemy.setDeltas(deltaX, deltaY);
		enemy.setX(x);
		enemy.setY(y);
	}


	void update(float deltaTime){
		if(ticCounter >=ticsToSpawn){
			ticsToSpawn *= spawnMultiplier;
			generate();
			ticCounter -= ticsToSpawn; 
		}
		int numOfEnemies = enemies.size();
		for(int i = 0; i < numOfEnemies; i++){
			BasicEnemy enemy = enemies.get(i);
			enemy.update(deltaTime); 
			enemy.hit(playerSpaceship);
			playerSpaceship.hitByMissles(enemy);
		}
		ticCounter += deltaTime; 
	}



	void present(Graphics g){
		int numOfEnemies = enemies.size();
		for(int i = 0; i < numOfEnemies; i++){
			BasicEnemy enemy = enemies.get(i);
			enemy.present(g);
		}
	}
	void destroy(){
		int numOfEnemies = enemies.size();
		for(int i = 0; i < numOfEnemies; i++){
			enemies.get(i).setLives(0);
			enemies.get(i).setState(State.DYING);
		}
		ticsToSpawn = 600f;
	}

	boolean isDead(){
		int numOfEnemies = enemies.size();
		for(int i = 0; i < numOfEnemies; i++){
			BasicEnemy enemy = enemies.get(i);
			if(!enemy.isDead())
				return false;
		}
		return true; 
	}

	void reset(){
		enemies.clear();
		ticsToSpawn = 1.2f;
		ticCounter = .5f;
		spawnMultiplier = .99f; 
	}
}
