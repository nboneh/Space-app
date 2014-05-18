package com.clouby.androidgames.space;


import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Graphics.PixmapFormat;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Screen;

public class LoadingScreen extends Screen{

	Pixmap developerLogo = null;
	boolean logoDisplayed = false; 

	public LoadingScreen(Game game){
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		if(!logoDisplayed)
			developerLogo = g.newPixmap("cloubylogo.png",PixmapFormat.ARGB8888);
		else{
			Assets.background = g.newPixmap("background.png", PixmapFormat.RGB565);
			Assets.playerSpaceShip = g.newPixmap("playerspaceship.png", PixmapFormat.ARGB4444);
			Assets.basicMissle = g.newPixmap("basicmissle.png", PixmapFormat.ARGB4444);
			Assets.playButton = g.newPixmap("playbutton.png", PixmapFormat.ARGB4444);
			Assets.highscoreButton = g.newPixmap("highscorebutton.png", PixmapFormat.ARGB4444);
			Assets.backButton = g.newPixmap("backbutton.png", PixmapFormat.ARGB4444);
			Assets.title = g.newPixmap("title.png", PixmapFormat.ARGB4444);

			try {
				Thread.sleep(2000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			game.setScreen(new MainMenuScreen(game));
		}
	}

	@Override
	public void present(float deltaTime) {	
		Graphics g = game.getGraphics();
		g.drawPixmap(developerLogo, 0, 0);
		logoDisplayed = true; 
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