package com.clouby.androidgames.space;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Screen;
import com.clouby.androidgames.space.object.World;

public class MainMenuScreen extends Screen{
	private World world; 

	public MainMenuScreen(Game game) {
		super(game);
		world = World.getInst();
	}

	@Override
	public void update(float deltaTime) {
		world.updateMainScreen(deltaTime, game.getInput());
	}

	@Override
	public void present(float deltaTime) {
		world.presentMainScreen(game.getGraphics());
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		
	}

}
