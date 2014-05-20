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
	private float ticsToSpawn = 2.0f;
	private  float ticCounter = 2.0f;
	private float spawnMultiplier = .999f; 


	EnemyGenerator(Spaceship playerSpaceship){
		enemies = new ArrayList<BasicEnemy>();
		rand = new Random(); 
		this.playerSpaceship = playerSpaceship; 
	}

	void generate(){

		int lives = rand.nextInt(5) + 1;
		float size = (rand.nextFloat() * 1 + .5f);
		int speed = (rand.nextInt(100) + 50);

		int numOfEnemies = enemies.size();
		BasicEnemy enemy = null;
		for(int i = 0; i < numOfEnemies; i++){
			BasicEnemy cycleEnemy = enemies.get(i);
			if(cycleEnemy.isDead()){
				enemy = cycleEnemy; 
				enemy.recycle(speed, size, lives);
				break; 
			}

		}
		if(enemy == null)
			enemy = new BasicEnemy(speed, size, lives);
		enemies.add(enemy);

		int x =0;
		int y =0 ; 
		float deltaX = rand.nextFloat() *2 -1;
		float deltaY = (float) Math.sqrt(1 - deltaX * deltaX); 
		if(rand.nextBoolean())
			deltaY = -deltaY; 

		int generateRegion = rand.nextInt(4);
		switch(generateRegion){
		case 0:
			x = rand.nextInt(Settings.WORLD_WIDTH);
			y = enemy.getTopBoundary();
			break; 
		case 1:
			x = enemy.getRightBoundary();
			y = rand.nextInt(Settings.WORLD_HEIGHT);
			break;
		case 2:
			x = rand.nextInt(Settings.WORLD_WIDTH);
			y = enemy.getBottomBoundary();
			break;
		case 3:
			x = enemy.getLeftBoundary();
			y = rand.nextInt(Settings.WORLD_HEIGHT);
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
		ticsToSpawn = 2.0f;
		ticCounter = 2.0f;
		spawnMultiplier = .999f; 
	}
}
