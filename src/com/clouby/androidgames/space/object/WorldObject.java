package com.clouby.androidgames.space.object;


import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.clouby.androidgames.space.Settings;

class WorldObject {

	protected final static int MAX_ALPHA = 255; 
	private int fadeSpeed = 60; 

	private Pixmap pixmap; 

	private int topBoundary;
	private int leftBoundary;
	private int rightBoundary;
	private int bottomBoundary;
	protected float distanceTraveled; 

	protected State state; 
	protected int width;
	protected int height;
	protected float x;
	protected float y;
	protected int frame; 
	protected float angle; 
	protected int speed; 
	protected float alpha; 
	private float deltaX;
	private float deltaY; 
	protected boolean dead; 
	private int attackFrame;
	private int dyingFrame;
	private int numOfFrames;
	private float ticChecker; 
	protected boolean tic; 
	private boolean fadeIn;
	private boolean fadeOut; 
	private int frameInNextTic = -1; 

	WorldObject(int x, int y, Pixmap pixmap, int speed
			,int attackFrame, int deathFrame, int numOfFrames){
		init( x, y, pixmap, speed, attackFrame,  deathFrame,  numOfFrames);
	}

	void recycle(int x, int y, Pixmap pixmap, int speed
			,int attackFrame, int deathFrame, int numOfFrames){
		init( x, y, pixmap, speed,  attackFrame,  deathFrame,  numOfFrames);

	}

	void init(int x, int y, Pixmap pixmap, int speed
			,int attackFrame, int deathFrame, int numOfFrames){
		this.width = (pixmap.getWidth() - numOfFrames + 1)/numOfFrames;
		this.x = x;
		this.y = y; 
		this.height = pixmap.getHeight();	
		this.pixmap = pixmap; 
		frame = 0;
		angle = 0; 
		this.speed = speed; 
		deltaX =0;
		deltaY = 0;  
		topBoundary = -height;
		leftBoundary = -width;
		rightBoundary = Settings.WORLD_WIDTH;
		bottomBoundary = Settings.WORLD_HEIGHT;
		alpha = MAX_ALPHA; 
		state = State.NORMAL; 
		distanceTraveled = 0 ; 
		dead = false; 
		fadeSpeed = 60;
		this.attackFrame = attackFrame;
		this.dyingFrame = deathFrame; 
		ticChecker = 0;
		this.numOfFrames = numOfFrames; 
		fadeIn = false;
		fadeOut = false; 
		tic = false;
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

		if(deltaY > 1)
			deltaY = 1;
		else if(deltaY < -1)
			deltaY = -1;

		if(deltaX > 1)
			deltaX = 1;
		else if(deltaX < -1)
			deltaX = -1;

	}

	void update(float deltaTime){

		if(!dead){

			if(fadeIn && alpha < MAX_ALPHA ){
				if(alpha < WorldObject.MAX_ALPHA){
					alpha += deltaTime * fadeSpeed;
					if(alpha >= MAX_ALPHA)
						fadeIn = false; 
					setAlpha(alpha);
				} 
			}

			if(fadeOut && alpha > 0 ){
				alpha -= deltaTime * fadeSpeed;
				if(alpha <= 0)
					fadeOut = false;
				setAlpha(alpha);
			}


			ticChecker += deltaTime;	
			if(ticChecker >= World.TIC_TIME){
				if(frameInNextTic >= 0){
					frame = frameInNextTic;
					frameInNextTic = -1; 
				}
				else
					frame++;
				switch(state){
				case NORMAL:
					if(frame >= attackFrame)
						frame = 0; 
					break;
				case ATTACKING:
					if(frame >= dyingFrame)
						setState(State.NORMAL);
					break;
				case DYING:
					if(frame >= (numOfFrames)){
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
				&& (x +width > objX) 
				&& (x <  objX+ object.getWidth())
				&& (y +height > objY )  
				&& (y < objY+object.getHeight())){
			object.setState(State.DYING);
			return true;
		}
		return false; 
	}

	void present(Graphics g){
		if(!dead){
			g.drawPixmap(pixmap, (int)x, (int)y, (frame * width  + frame ), 0, width, height, (int)angle, (int) alpha);
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
		case ATTACKING:
			frameInNextTic = attackFrame; 
			break;
		case DYING:
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

	void setAlpha(float alpha){
		if(alpha > MAX_ALPHA)
			alpha = MAX_ALPHA;
		else if(alpha < 0)
			alpha = 0; 
		this.alpha = alpha; 
	}

	void putInCenterX(){
		x = rightBoundary/2 - width/2; 
	}

	void putInCenterY(){
		y = bottomBoundary/2 - height/2; 
	}

	enum State {
		NORMAL, 
		ATTACKING, 
		DYING; 
	}

	void fadeIn(){
		alpha = 0;
		fadeIn = true;
	}

	boolean isFadeIn(){
		return fadeIn; 
	}
	void fadeOut(){
		fadeOut = true; 
	}
}
