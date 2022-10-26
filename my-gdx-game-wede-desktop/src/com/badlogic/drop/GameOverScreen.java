package com.badlogic.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen implements Screen {
	Drop game = new Drop();
	
	OrthographicCamera camera;
	
	Music titleScreenMusic;
	Sound clickSound;
	Texture raindropImage;
	
	GameOverScreen(final Drop game){
		this.game = game;
		
		raindropImage = new Texture(Gdx.files.internal("rainnyy.png"));
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 400);
		titleScreenMusic = Gdx.audio.newMusic(Gdx.files.internal("titleScreen.mp3"));
		//clickSound = Gdx.audio.newSound(Gdx.files.internal("sound.mp4"));
	}
	
	public void show() {
		titleScreenMusic.setLooping(true);
		titleScreenMusic.play();
		
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.1f, 0);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		game.font.getData().setScale(4);
		game.font.draw(game.batch, "Game Over!", 244, 240);
		game.batch.end();
		
		if(Gdx.input.isTouched()) {
			titleScreenMusic.stop();
			game.setScreen(new GameScreen(game));
			dispose();
		}
		
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
  