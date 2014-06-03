package com.clouby.androidgames.space;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class MainActivity extends AndroidGame {

    @Override
    public Screen getStartScreen(){
    	return new LoadingScreen(this);
    }
}
