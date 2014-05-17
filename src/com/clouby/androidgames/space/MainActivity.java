package com.clouby.androidgames.space;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.coluby.androidgames.codebreaker.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends AndroidGame {

    @Override
    public Screen getStartScreen(){
    	return new LoadingScreen(this);
    }
}
