package com.mygdx.game;

import com.badlogic.gdx.*;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.utils.viewport.ScreenViewport;



public class PirateJoes extends Game {

	public static final int WIDTH = 480;
	public static final int HEIGHT = 270;

	public static final int ASPECT_H = 10;
	public static final int ASPECT_W = 5;

	public static final Vector3 mouseCordinates = new Vector3();


	SpriteBatch batch;



	Stage stage;











	@Override
	public void create () {

		System.out.println(System.getProperty("user.dir"));





		//setScreen(new MainMenu());

		batch = new SpriteBatch();




		setScreen(new MainMenu(this));







		// final Dialog dialog = new Dialog("You Clicked", skin);












/*
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dialog.show(welcomeScreen);
				Timer.schedule(new Timer.Task() {
					@Override
					public void run() {
						dialog.hide();
					}
				}, 3f);
			}
		});


 */








 // the stage sends out input

		// stage is a container of Actors

		//stage.addActor(new ActorFake(new Texture(Gdx.files.internal("badlogic.jpg"))));
		//stage.addActor(new Character());

   // this makes the stage focus on the keyboard from the actor charecter

	}


	
	@Override
	public void dispose () {

		batch.dispose();
	}


	}
