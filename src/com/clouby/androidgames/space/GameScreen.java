package com.clouby.androidgames.space;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Screen;
import com.clouby.androidgames.space.object.World;
import com.clouby.androidgames.space.object.World.WorldState;

public class GameScreen extends Screen{
	private World world; 

	public GameScreen(Game game) {
		super(game);
		world = World.getInst(game.getHandler());
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
		if(world.getWorldState() == WorldState.GAME)
			world.setWorldState(WorldState.GAMEOVER);
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		
	}

}
