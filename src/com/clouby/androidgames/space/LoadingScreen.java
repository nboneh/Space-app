package com.clouby.androidgames.space;


import com.badlogic.androidgames.framework.Audio;
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
		Audio a = game.getAudio();
		if(!logoDisplayed)
			developerLogo = g.newPixmap("cloubylogo.png",PixmapFormat.ARGB8888);
		else{
			Assets.background = g.newPixmap("background.png", PixmapFormat.RGB565);
			Assets.playerSpaceShip = g.newPixmap("playerspaceship.png", PixmapFormat.ARGB4444);
			Assets.basicPlayerMissle = g.newPixmap("basicplayermissle.png", PixmapFormat.ARGB4444);
			Assets.playButton = g.newPixmap("playbutton.png", PixmapFormat.ARGB4444);
			Assets.highscoreButton = g.newPixmap("highscorebutton.png", PixmapFormat.ARGB4444);
			Assets.backButton = g.newPixmap("backbutton.png", PixmapFormat.ARGB4444);
			Assets.title = g.newPixmap("title.png", PixmapFormat.ARGB4444);
			Assets.basicEnemy = g.newPixmap("basicenemy.png", PixmapFormat.ARGB4444);
			Assets.phoneOrientation = g.newPixmap("phoneorientation.png", PixmapFormat.ARGB4444);
			Assets.highscoreTitle = g.newPixmap("highscoretitle.png", PixmapFormat.ARGB4444);
			Assets.onlineTitle = g.newPixmap("onlinetitle.png", PixmapFormat.ARGB4444);
			Assets.localTitle = g.newPixmap("localtitle.png", PixmapFormat.ARGB4444);
			Assets.gameOver = g.newPixmap("gameover.png",PixmapFormat.ARGB4444);
			Assets.newGame = g.newPixmap("newgame.png",PixmapFormat.ARGB4444);
			Assets.mainMenu = g.newPixmap("mainmenu.png",PixmapFormat.ARGB4444);
			Assets.submitButton = g.newPixmap("mainmenu.png", PixmapFormat.ARGB4444);
			Assets.font = g.newTypeFace("font.ttf");
			Assets.basicMissleFire = a.newSound("basicmisslefire.ogg");
			Assets.forming = a.newSound("forming.ogg");
			Assets.deForming = a.newSound("deforming.ogg");

			try {
				Thread.sleep(2000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			game.setScreen(new GameScreen(game));
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