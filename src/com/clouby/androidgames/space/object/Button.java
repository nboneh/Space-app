package com.clouby.androidgames.space.object;

import com.badlogic.androidgames.framework.Pixmap;

public class Button extends WorldObject {	
	public Button(int x, int y, Pixmap pixmap) {
		super(x, y, pixmap, 0, 1, 1, 4);
		setState(State.INTRO);
	}
}
