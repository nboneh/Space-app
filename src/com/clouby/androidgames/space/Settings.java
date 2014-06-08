package com.clouby.androidgames.space;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.os.Handler;
import android.os.Message;

import com.badlogic.androidgames.framework.FileIO;

public class Settings {
	private final static String FILE_NAME = ".retrospace";
	public static final int WORLD_WIDTH = 480;
	public static final int WORLD_HEIGHT = 320;

	public static String name = "Clouby";

	public static float defaultX = 0;
	public static float defaultY = 0; 

	public static HighScoreContainer[] localHighscores;
	public final static int size = 5; 

	private static final String PASSWORD = "asaf111";
	private static final String WEB_URL ="http://retrospacescores.appspot.com/highscore";

	static 
	{
		localHighscores = new HighScoreContainer[5]; 
		for(int i = 0; i < size; i++){
			localHighscores[i] = new HighScoreContainer();
		}
	}

	public static void load(FileIO files){
		BufferedReader in = null;
		try{
			in = new BufferedReader
					(new InputStreamReader(files.readFile(FILE_NAME)));
			name = in.readLine();
			defaultX = Float.parseFloat(in.readLine());
			defaultY = Float.parseFloat(in.readLine());
			for(int i = 0; i < size; i++){
				HighScoreContainer score = localHighscores[i]; 
				score.setName(in.readLine());
				score.setScore(Integer.parseInt(in.readLine()));
				score.setTimestamp(Long.parseLong(in.readLine()));
				score.setSent(Boolean.parseBoolean(in.readLine()));
			}
		} catch (IOException e){
			//We have default highscores
		} catch (NumberFormatException e){
			//DEFAULTS!!
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
					files.writeFile(FILE_NAME)));
			out.write(name);
			out.write(System.getProperty("line.separator"));
			out.write(Float.toString(defaultX));
			out.write(System.getProperty("line.separator"));
			out.write(Float.toString(defaultY));
			out.write(System.getProperty("line.separator"));
			for(int i = 0; i < size; i++){
				HighScoreContainer score = localHighscores[i]; 
				out.write(score.getName());
				out.write(System.getProperty("line.separator"));
				out.write(Integer.toString(score.getScore()));
				out.write(System.getProperty("line.separator"));
				out.write(Long.toString(score.getTimestamp()));
				out.write(System.getProperty("line.separator"));
				out.write(Boolean.toString(score.isSent()));
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

	public static void loadOnlineHighScores(final Handler h){
		Runnable r = new Runnable(){
			@Override
			public void run() {
				try{
					URL website = new URL(WEB_URL);

					URLConnection connection = website.openConnection();
					BufferedReader in = new BufferedReader(
							new InputStreamReader(
									connection.getInputStream()));

					StringBuilder response = new StringBuilder();
					String inputLine;

					while ((inputLine = in.readLine()) != null) 
						response.append(inputLine);

					in.close();
					Message msg = new Message();
					msg.obj = response.toString();
					h.sendMessage(msg);
				} catch (MalformedURLException e) {
				} catch (IOException e) {
				}
			}
		}; 
		Thread t = new Thread(r);
		t.start(); 
	}

	public static boolean madeHighScore(int score){
		for(int i = 0; i < size; i++){
			if(localHighscores[i].getScore() < score){
				return true;
			}
		}
		return false; 
	}

	public static void addScore(int score, String name){
		Settings.name = name; 
		for(int i = 0; i < size; i++){
			if(localHighscores[i].getScore() < score){
				for(int j = size-1; j > i; j--){
					localHighscores[j] = localHighscores[j-1];
				}
				HighScoreContainer scoree = new HighScoreContainer(); 
				scoree.setScore(score);
				scoree.setTimestamp(System.currentTimeMillis());
				scoree.setSent(false);
				scoree.setName(name);
				localHighscores[i] = scoree; 
				break; 
			}
		}
	}

	public static void updateOnlineScores(){
		Runnable r = new Runnable(){
			@Override
			public void run() {
				for(int i = 0; i < size; i ++){
					HighScoreContainer scoreCont= localHighscores[i];
					if(!scoreCont.isSent()){
						String urlParameters = "&date=" + scoreCont.getTimestamp()
								+ "&score=" + scoreCont.getScore()
								+ "&name=" + scoreCont.getName()
								+ "&pass=" + PASSWORD;
						try{
							URL url = new URL(WEB_URL);
							HttpURLConnection conn=  (HttpURLConnection) url.openConnection();    
							conn.setRequestMethod("POST");
							conn.setDoOutput(true);
							OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

							writer.write(urlParameters);
							writer.flush();

							conn.getResponseCode();
							writer.close(); 

							scoreCont.setSent(true);

						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} 
					}
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
}