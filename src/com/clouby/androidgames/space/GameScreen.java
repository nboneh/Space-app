package com.clouby.androidgames.space;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Screen;
import com.clouby.androidgames.space.object.World;

public class GameScreen extends Screen{
	private World world; 

	public GameScreen(Game game) {
		super(game);
		world = World.getInst();
	}

	@Override
	public void update(float deltaTime) {
		world.update(deltaTime, game.getInput());
	}

	@Override
	public void present(float deltaTime) {
		world.present(game.getGraphics());
	}

	@Override
	public void pause() {
		Settings.save(game.getFileIO());
	}

	@Override
	public void resume() {
		Settings.load(game.getFileIO());
	}

	@Override
	public void dispose() {
		
	}

}
