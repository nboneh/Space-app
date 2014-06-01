package com.clouby.androidgames.space;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

import android.util.Log;

import com.badlogic.androidgames.framework.FileIO;

public class Settings {
	public static final int WORLD_WIDTH = 480;
	public static final int WORLD_HEIGHT = 320;
	
	public static float defaultX = 0;
	public static float defaultY = 0; 

	public static int[] highscores;
	public static String[] names;
	public final static int size = 5; 
	private final static String fileName = ".retrospace";

	static 
	{
		highscores = new int[size];
		Arrays.fill(highscores, 0);
		names = new String[size];
		Arrays.fill(names, "Clouby");
	}

	public static void load(FileIO files){
		BufferedReader in = null;
		try{
			in = new BufferedReader
					(new InputStreamReader(files.readFile(fileName)));
			defaultX = Float.parseFloat(in.readLine());
			defaultY = Float.parseFloat(in.readLine());
			for(int i = 0; i < size; i++){
				highscores[i] = Integer.parseInt(in.readLine());
			}
			for(int i = 0; i < size; i++){
				names[i] = in.readLine();
			}
		} catch (IOException e){
			//We have default highscores
		} catch (NumberFormatException e){
			//DEFAULTS!!!
			Log.d("Load", "Numberformat");
		} finally{
			try{
				if(in != null)
					in.close();
			}catch(IOException e){
			}
		}
	}

	public static void save(FileIO files){
		BufferedWriter out = null;
		try{
			out = new BufferedWriter(new OutputStreamWriter(
					files.writeFile(fileName)));
			out.write(Float.toString(defaultX));
			out.write(System.getProperty("line.separator"));
			out.write(Float.toString(defaultY));
			out.write(System.getProperty("line.separator"));
			for(int i = 0; i < size; i++){
				out.write(Integer.toString(highscores[i]));
				out.write(System.getProperty("line.separator"));
			} 
			for(int i = 0; i < size; i++){
				out.write(names[i]);
				out.write(System.getProperty("line.separator"));
			}

		}catch(IOException e){
		} finally {
			try{
				if(out != null)
					out.close();
			} catch(IOException e){

			}
		}
	}

	public static boolean madeHighScore(int score){
		for(int i = 0; i < size; i++){
			if(highscores[i] < score){
				return true;
			}
		}
		return false; 
	}

	public static void addScore(int score, String name){
		if(!name.equals("")){
			for(int i = 0; i < size; i++){
				if(highscores[i] < score){
					for(int j = size-1; j > i; j--){
						highscores[j] = highscores[j-1];
						names[j] = names[j-1];
					}
					highscores[i] = score; 
					names[i] = name; 
					break; 
				}
			}
		}

	}
}

