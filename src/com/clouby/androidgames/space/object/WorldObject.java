package com.clouby.androidgames.space.object;


import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Sound;
import com.clouby.androidgames.space.Assets;
import com.clouby.androidgames.space.Settings;

abstract class WorldObject {

	private Pixmap pixmap; 
	
	private int topBoundary;
	private int leftBoundary;
	private int rightBoundary;
	private int bottomBoundary;
	protected float distanceTraveled; 

	protected State state; 
	protected int width;
	protected int height;
	protected int presentWidth;
	protected int presentHeight;
	protected float x;
	protected float y;
	protected int frame; 
	protected float angle; 
	protected int speed; 
	private float deltaX;
	private float deltaY; 
	protected boolean dead; 
	private int attackFrame;
	private int dyingFrame;
	private int numOfFrames;
	private float ticChecker; 
	protected boolean tic; 
	private int frameInNextTic = -1; 
	protected int lives = 1;
	protected float volume; 
	protected Sound nextSound; 
	protected float size; 


	WorldObject(int x, int y, Pixmap pixmap, int speed
			,int attackFrame, int deathFrame, int numOfFrames, float size){
		init( x, y, pixmap, speed, attackFrame,  deathFrame,  numOfFrames, size);
	}

	void recycle(int x, int y, Pixmap pixmap, int speed
			,int attackFrame, int deathFrame, int numOfFrames, float size){
		init( x, y, pixmap, speed,  attackFrame,  deathFrame,  numOfFrames, size);

	}

	void init(int x, int y, Pixmap pixmap, int speed
			,int attackFrame, int deathFrame, int numOfFrames, float size){
		this.size=  size; 
		this.presentWidth = (pixmap.getWidth() - numOfFrames + 1)/numOfFrames;
		this.x = x;
		this.y = y; 
		this.presentHeight = pixmap.getHeight();	
		height = (int) (presentHeight * size);
		width = (int) (presentWidth * size);
		this.pixmap = pixmap; 
		angle = 0; 
		this.speed = speed; 
		deltaX =0;
		deltaY = 0;  
		topBoundary = -height;
		leftBoundary = -width;
		rightBoundary = Settings.WORLD_WIDTH;
		bottomBoundary = Settings.WORLD_HEIGHT;
		distanceTraveled = 0 ; 
		this.attackFrame = attackFrame;
		this.dyingFrame = deathFrame; 
		ticChecker = 0;
		this.numOfFrames = numOfFrames; 
		tic = false;
		volume = .3f;
		setState(State.INTRO);
	}

	int getWidth() {
		return width;
	}
	int getHeight() {
		return height;
	}
	int getX() {
		return (int) x;
	}
	int getY() {
		return (int) y;
	}

	public void setDeltas(float deltaX, float deltaY){
		this.deltaX = deltaX;
		this.deltaY = deltaY; 
	}

	void update(float deltaTime){

		if(!dead){
			if(nextSound != null){
				nextSound.play(volume);
				nextSound = null; 
			}
			ticChecker += deltaTime;	
			if(ticChecker >= World.TIC_TIME){
				if(frameInNextTic >= 0){
					frame = frameInNextTic;
					frameInNextTic = -1; 
				}
				else
					if(state != State.INTRO)
						frame++;
					else 
						frame--;
				switch(state){
				case INTRO:
					if(frame <= (dyingFrame ))
						setState(State.NORMAL);
					break; 
				case NORMAL:
					if(frame >= attackFrame)
						frame = 0; 
					break;
				case ATTACKING:
					if(frame >= (dyingFrame -1))
						setState(State.NORMAL);
					break;
				case DYING:
					if(lives > 0)
						setState(State.NORMAL);
					else if(frame >= (numOfFrames)){
						frame = (numOfFrames -1);
						dead = true; 
					}
					break;
				default:
					break;

				}
				ticChecker -= World.TIC_TIME;
				tic = true; 
			} else {
				tic = false; 
			}

			float changeX = deltaX * deltaTime * speed; 
			float changeY = deltaY * deltaTime * speed;
			distanceTraveled += Math.abs(changeX) + Math.abs(changeY); 

			x += changeX; 
			y += changeY; 

			if(x < leftBoundary)
				x = rightBoundary;
			else if (x >= rightBoundary)
				x = leftBoundary;

			if(y < topBoundary)
				y = bottomBoundary;
			else if (y >= bottomBoundary)
				y = topBoundary;	
		} 
	}


	void updateAngle(int refX, int refY){ 
		float xDist =  Math.abs(refX - x);
		float yDist =  Math.abs(refY - y); 
		float calAngle = angle;
		if(refY < y && refX >= x || refY >= y && refX < x){
			//1st quad and 3rd quad 
			if(yDist != 0)
				calAngle  = (int) Math.toDegrees(Math.atan(xDist/yDist));
			else
				calAngle  = 90;

			if(refY >= y && refX < x){ 
				//3rd quad
				calAngle  += 180;
			}
		} else{
			//2nd quad and 4th quad 
			if(xDist != 0)
				calAngle  =(int) Math.toDegrees(Math.atan(yDist/xDist)) + 90;
			else
				calAngle  = 180;

			if(refY < y && refX < x)
				//4th quad
				calAngle  += 180; 
		}
		setAngle(calAngle);
	}

	void setAngle(float angle){ 

		this.angle = angle; 
	}

	float getAngle(){
		return angle;
	}

	boolean hit(WorldObject object){
		int objX = object.getX();
		int objY = object.getY();
		if (state != State.DYING
				&& object.state != State.DYING
				&& (x +(width) > objX) 
				&& (x <  objX+ (object.getWidth()))
				&& (y +(height) > objY )  
				&& (y < objY+(object.getHeight()))){
			object.setState(State.DYING);
			return true;
		}
		return false; 
	}
	
	boolean clicked(int finx, int finy){
		if (state != State.DYING 
				&& state != State.INTRO 
				&& (x +(width) > finx) 
				&& (x <  finx)
				&& (y +(height) > finy )  
				&& (y < finy)){
				return true; 
		}else
			return false; 
	}

	void present(Graphics g){
		if(!dead && frame < numOfFrames){
			g.drawPixmap(pixmap, (int)x, (int)y, (frame * presentWidth  + frame ), 0, presentWidth, presentHeight, (int)angle, size);
		}
	}



	boolean isDead(){
		return dead; 
	}

	State getState(){
		return state; 
	}

	void setState(State state){

		if(this.state != state){
			switch(state){
			case INTRO:
				//Intro is death in reverse 
				nextSound = Assets.forming;
				dead = false; 
				frame = numOfFrames; 
				frameInNextTic = (numOfFrames -1); 
				break;
			case ATTACKING:
				frameInNextTic = attackFrame; 
				break;
			case DYING:
				lives--;
				if(lives < 1)
					nextSound = Assets.deForming;
				frameInNextTic = dyingFrame;
				break;
			case NORMAL:
				frameInNextTic = 0; 
				break;
			default:
				break;

			}
			this.state = state; 
		}
	}


	void centerInX(){
		x = rightBoundary/2 - width/2; 
	}

	void centerInY(){
		y = bottomBoundary/2 - height/2; 
	}

	enum State {
		INTRO,
		NORMAL, 
		ATTACKING, 
		DYING; 
	}
	
	void setX(int x){
		this.x = x; 
	}

	void setY(int y) {
		this.y = y; 
	}
	void setLives(int lives){
		this.lives = lives; 
	}
	
	int getTopBoundary() {
		return topBoundary;
	}

	int getLeftBoundary() {
		return leftBoundary;
	}


	int getRightBoundary() {
		return rightBoundary;
	}



	int getBottomBoundary() {
		return bottomBoundary;
	}

	void setSize(float size){
		height = (int) (presentHeight * size);
		width = (int) (presentWidth * size);
		topBoundary = -height;
		leftBoundary = -width;
		this.size = size; 
	}
}
