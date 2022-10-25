package com.badlogic.drop;
import com.badlogic.gdx.Screen;

import java.util.ListIterator;
import com.badlogic.drop.Drop;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.SortedIntList.Iterator;
import com.badlogic.gdx.utils.SortedIntList.Node;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
public class GameScreen implements Screen {
	final Drop game;
	
	final int regularFallSpeed = 200;
	final int increasedFallSpeed = 300;
	
	Texture bucketImage;
	Texture raindropImage;
	Texture heartImage;
	Texture badDropImage;
	Sound dropSound;
	Sound loseHealthSound;
	Music backgroundMusic;
	Rectangle bucket;
	Array<Rectangle> raindrops;
	Array<Rectangle> badDrops;
	Array<Rectangle> goodDrops;
	Array<Rectangle> hearts;
	int lives;
	boolean increasedSpeed;
	ListIterator<Rectangle> badIterator;
	OrthographicCamera camera; //used for camera
	long lastDropTime;
	long lastDropTimeBad;
	long lastDropTimeBadCaught;
	int points;
	int random;
	int fallSpeed;
	

	public GameScreen(final Drop game) {
		this.game = game;
		
		bucketImage = new Texture(Gdx.files.internal("buckety.png"));
		raindropImage = new Texture(Gdx.files.internal("rainnyy.png"));
		heartImage = new Texture(Gdx.files.internal("heart.png"));
		badDropImage = new Texture(Gdx.files.internal("baddy.png"));
		dropSound = Gdx.audio.newSound(Gdx.files.internal("waterdrop.wav"));
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("backgroundmusic.mp3"));
		loseHealthSound = Gdx.audio.newSound(Gdx.files.internal("loseHealthSound.wav"));
		
		fallSpeed = 200;
		
		bucket = new Rectangle();
		increasedSpeed = false;
		bucket.width = 72;
		bucket.height = 64;
		bucket.x = 800/2 - 64/2;
		bucket.y = 0;
		camera = new OrthographicCamera();
		points = 0;
		raindrops = new Array<Rectangle>();
		badDrops = new Array<Rectangle>();
		goodDrops = new Array<Rectangle>();
		hearts = new Array<Rectangle>(3);
		lives = 3;
		hearts(670);
		hearts(710);
		hearts(750);
		addRaindrops();
		backgroundMusic.setLooping(true);
		backgroundMusic.play();
		camera.setToOrtho(false, 800, 400);
		
		
	}
	public void hearts(int xPos) {
		Rectangle heart = new Rectangle();
		heart.height = 64;
		heart.width = 64;
		heart.x = xPos;
		heart.y = 360;
		hearts.add(heart);
	}
	public void addRaindrops() {
		Rectangle raindrop = new Rectangle();
		raindrop.height = 64;
		raindrop.width = 64;
		raindrop.x = MathUtils.random(0, 800-64);
		raindrop.y = 480;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
		
	}
	
	public void addBadDrop() {
		Rectangle badDrop = new Rectangle();
		badDrop.height = 64;
		badDrop.width = 64;
		badDrop.x = MathUtils.random(0, 800-64);
		badDrop.y = 480;
		badDrops.add(badDrop);
		lastDropTimeBad = TimeUtils.nanoTime();
	}
	
	public int getRandom() {
		random = MathUtils.random(0, 5);
		
		return random;
	}
	
	public void dispose() {
		raindropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		backgroundMusic.dispose();
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, .1f, 0);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);	
		
		game.batch.begin();
		
		game.font.getData().setScale(2);
		game.font.draw(game.batch, "Dropper", 300, 390);
		game.font.draw(game.batch, String.valueOf(points), 390, 340);
		
		game.batch.draw(bucketImage, bucket.x, bucket.y);
		
		for(Rectangle heart: hearts) {
			game.batch.draw(heartImage, heart.x, heart.y);
		}
		
		for(Rectangle raindrop: raindrops) {
			game.batch.draw(raindropImage, raindrop.x, raindrop.y);
		}
		
		for(Rectangle badDrop: badDrops) {
			game.batch.draw(badDropImage, badDrop.x, badDrop.y);
		}
		
		game.batch.end();
		
		if(lives == 0) {
			game.setScreen(new GameOverScreen(game));
			backgroundMusic.stop();
		}
		
		if(increasedSpeed) {
			fallSpeed = 900;
		}

		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64/2;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			bucket.x += 200 * Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		}
		if(bucket.x >= 800 - 64) {
			bucket.x = 800 - 64;
		}
		if(bucket.x < 0) {
			bucket.x = 0;
		}
		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) {
			addRaindrops();
		}
		
		 
		for(ArrayIterator<Rectangle> iter = raindrops.iterator(); iter.hasNext();) {
			
			if(MathUtils.random(0, 200) == 1 && TimeUtils.nanoTime() - lastDropTimeBad > 1000000000 ) {
				addBadDrop();
				System.out.println("baddrop");			
			}
			Rectangle raindrop = iter.next();
			raindrop.y -= fallSpeed * Gdx.graphics.getDeltaTime();
			if(raindrop.y  + 64 < 0) {
				loseHealthSound.play();
				iter.remove();
				lives = lives - 1;
				hearts.removeIndex(lives);

	
			}
			if(raindrop.overlaps(bucket)) {
				dropSound.play();
				points++;
				iter.remove();
				
			}
		}
		

		 
		for(ArrayIterator<Rectangle> iter = badDrops.iterator(); iter.hasNext();){
			Rectangle badDrop = iter.next();
			
			badDrop.y -= 300 * Gdx.graphics.getDeltaTime();
			
			if(badDrop.overlaps(bucket)) {
				iter.remove();
				increasedSpeed = true;
			}
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
	
	
	

}
