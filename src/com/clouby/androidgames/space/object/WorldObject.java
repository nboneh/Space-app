package com.clouby.androidgames.space.object;


import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.clouby.androidgames.space.Settings;

abstract class WorldObject {

	protected final static int MAX_ALPHA = 255; 

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
	private int frame; 
	protected float angle; 
	protected int speed; 
	protected float alpha; 
	private float deltaX;
	private float deltaY; 
	protected boolean dead; 

	WorldObject(int x, int y, int numOfFrames, Pixmap pixmap, int speed, float deltaX, float deltaY){
		init( x, y, numOfFrames, pixmap, speed,  deltaX, deltaY);
	}

	void recycle(int x, int y, int numOfFrames, Pixmap pixmap, int speed, float deltaX, float deltaY){
		init( x, y, numOfFrames, pixmap, speed,  deltaX, deltaY);

	}

	void init(int x, int y, int numOfFrames, Pixmap pixmap, int speed, float deltaX, float deltaY){
		this.width = (pixmap.getWidth() + numOfFrames - 1)/numOfFrames;
		this.x = x;
		this.y = y; 
		this.height = pixmap.getHeight();	
		this.pixmap = pixmap; 
		frame = 0;
		angle = 0; 
		this.speed = speed; 
		this.deltaX = deltaX;
		this.deltaY = deltaY;  
		topBoundary = -height;
		leftBoundary = -width;
		rightBoundary = Settings.WORLD_WIDTH;
		bottomBoundary = Settings.WORLD_HEIGHT;
		alpha = MAX_ALPHA; 
		state = State.NORMAL; 
		distanceTraveled = 0 ; 
		dead = false; 

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
		return (
				(x +width > objX) 
				&& (x <  objX+ object.getWidth())
				&& (y +height > objY )  
				&& (y < objY+object.getHeight()));
	}

	void present(Graphics g){
		if(!dead){
			g.drawPixmap(pixmap, (int)x, (int)y, (frame * width  + frame ), 0, width, height, (int)angle, (int) alpha);
		}
	}


	boolean isDead(){
		return dead; 
	}

	enum State {
		INTRO,
		NORMAL, 
		ATTACKING,
		DYING; 
	}
}
