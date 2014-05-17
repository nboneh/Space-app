package com.clouby.androidgames.space.object;

import java.util.List;


import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.clouby.androidgames.space.Assets;
import com.clouby.androidgames.space.object.WorldObject.State;


public class World {

	private WorldObject playerSpaceship; 
	final static float TIC_TIME = 0.05f; 
	private static World inst;

	public static World getInst(){
		if(inst == null)
			inst = new World();
		return inst; 
	}

	private World(){
		playerSpaceship = new Spaceship(0, 0, 175);
		playerSpaceship.putInCenterX();
		playerSpaceship.putInCenterY(); 
		playerSpaceship.fadeIn();
	}

	public void updateMainScreen(float deltaTime, Input input){
		updatePlayerSpaceship(deltaTime, input);

	}

	public void presentMainScreen(Graphics g){
		g.drawPixmap(Assets.background, 0, 0);
		playerSpaceship.present(g);
	}

	private void updatePlayerSpaceship(float deltaTime, Input input){
		if(!playerSpaceship.isFadeIn()){
			//Inputs should be flipped 
			float deltaX =input.getAccelY();
			//Minus so you can hold the phone tilted
			float deltaY = input.getAccelX() - 3;
			float hyp = (float) Math.sqrt(deltaX*deltaX + deltaY*deltaY); 
			if(hyp > 1){
				//Cap speed
				deltaX /= hyp;
				deltaY /= hyp; 
			}

			playerSpaceship.setDeltas(deltaX, deltaY);

			List<TouchEvent> touchEvents = input.getTouchEvents();
			int size = touchEvents.size();
			for(int i = 0; i < size; i++){
				TouchEvent event = touchEvents.get(i);
				switch(event.type){
				case TouchEvent.TOUCH_DOWN:
					playerSpaceship.setState(State.ATTACKING);
				case TouchEvent.TOUCH_DRAGGED:
					playerSpaceship.updateAngle(event.x, event.y);
					break; 
				}
			}
		}

		playerSpaceship.update(deltaTime);

	}

}
