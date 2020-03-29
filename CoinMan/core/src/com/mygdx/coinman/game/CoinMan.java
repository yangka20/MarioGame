package com.mygdx.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.ArrayList;
import java.util.Random;

import javax.xml.soap.Text;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] Background;
	int bgCount;




	Texture[] man;
	int manState = 0; //determine the running prosition of the man
	int pause = 0; //control the speed of running

	float gravity = 0.3f; //control the man fall
	float velocity = 0; // control the man moving
	int manY = 0; //man initial place
	Rectangle manRectangle;

	//coin posistion
	ArrayList<Integer> coinXs = new ArrayList<Integer>();
	ArrayList<Integer> coinYs = new ArrayList<Integer>();
	ArrayList<Rectangle> coinRectangle = new ArrayList<Rectangle>();
	Texture coin;
	int coinCount;

	ArrayList<Integer> bombXs = new ArrayList<Integer>();
	ArrayList<Integer> bombYs = new ArrayList<Integer>();
	ArrayList<Rectangle> bombRectangle = new ArrayList<Rectangle>();
	Texture bomb;
	int bombCount;

	Texture cake;
	ArrayList<Integer> cakeXs = new ArrayList<Integer>();
	ArrayList<Integer> cakeYs = new ArrayList<Integer>();
	ArrayList<Rectangle> cakeRectangle = new ArrayList<Rectangle>();
	int cakeCount;

	int score = 0;
	BitmapFont font;

	Random random;

	int gameState = 0;

	Texture dizzy;
	Texture win;
	Texture princess;

	String scoreString;

	@Override
	public void create () {
		batch = new SpriteBatch();
		//background = new Texture("bg2.png");
		Background = new Texture[4];
		man = new Texture[4]; //image, man is running
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");

		Background[0] = new Texture("bg.png");
		Background[1] = new Texture("bg3.png");
		Background[2] = new Texture("bg2.png");
		Background[3] = new Texture("bg-princess.png");

		manY = Gdx.graphics.getHeight()/2;

		coin = new Texture("Canned.png");
		bomb = new Texture("bomb.png");
		cake = new Texture("cake.png");

		random = new Random();

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		dizzy = new Texture("dizzy-1.png");
		win = new Texture("frame-win.png");
		bgCount = 0;
		princess = new Texture("frame-princess.png");

		scoreString = String.valueOf(score);

	}

	//method to make multiply coin
	public void makeCoin(){
		float height = random.nextFloat()*Gdx.graphics.getHeight();
		coinYs.add((int)height);
		coinXs.add(Gdx.graphics.getWidth()); //x position is fix
	}

	//method to make multiply bomb
	public void makeBomb(){
		float height = random.nextFloat()*Gdx.graphics.getHeight();
		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth()); //x position is fix
	}


	public void makeCake(){
		float height = random.nextFloat()*Gdx.graphics.getHeight();
		cakeYs.add((int)height);
		cakeXs.add(Gdx.graphics.getWidth()); //x position is fix
	}

	@Override
	public void render () {
		batch.begin();
		//show image inside the app,order is important


		if(bgCount <5){
			batch.draw(Background[0],0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		}else if(bgCount >=5&&bgCount<10){
			batch.draw(Background[1],0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		}else if(bgCount >=10&&bgCount<15){
			batch.draw(Background[2],0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		}else if(bgCount == 15){
			bgCount = 0;
		}



		if(gameState ==1){
			//game is live
			//bomb
			if (bombCount < 250){
				bombCount++;
			}else {
				bombCount = 0;
				makeBomb();//add coin
			}

			bombRectangle.clear();
			for(int i = 0; i<bombXs.size();i++){
				batch.draw(bomb,bombXs.get(i),bombYs.get(i));
				bombXs.set(i,bombXs.get(i) -8);
				bombRectangle.add(new Rectangle(bombXs.get(i),bombYs.get(i),bomb.getWidth(),bomb.getHeight()));
			}

			//coins
			if (coinCount < 100){
				coinCount++;
			}else {
				coinCount = 0;
				makeCoin();//add coin
			}


			coinRectangle.clear();
			for(int i = 0; i<coinXs.size();i++){
				batch.draw(coin,coinXs.get(i),coinYs.get(i));
				coinXs.set(i,coinXs.get(i) -4);
				coinRectangle.add(new Rectangle(coinXs.get(i),coinYs.get(i),coin.getWidth(),coin.getHeight()));
			}

			//cake
			if (cakeCount < 200){
				cakeCount++;
			}else {
				cakeCount = 0;
				makeCake();//add cake
			}


			cakeRectangle.clear();
			for(int i = 0; i<cakeXs.size();i++){
				batch.draw(cake,cakeXs.get(i),cakeYs.get(i));
				cakeXs.set(i,cakeXs.get(i) -7);
				cakeRectangle.add(new Rectangle(cakeXs.get(i),cakeYs.get(i),cake.getWidth(),cake.getHeight()));
			}


			//the man jump
			if(Gdx.input.justTouched()){
				velocity = -10; //touch the man will jump up
			}

			if(pause <8){
				pause ++;
			}else{
				pause = 0;
				if(manState <3){
					manState++;
				}else{
					manState = 0;
				}
			}

			velocity += gravity;
			manY -= velocity;

			if(manY <=0){
				manY = 0; //ensure man stop at the bottom
			}
		}else if(gameState ==0){
			//waiting to start
			if(Gdx.input.justTouched()){
				gameState =1;
			}
		}else if(gameState == 2){
			//game over
			if(Gdx.input.justTouched()){
				gameState =1;
				manY = Gdx.graphics.getHeight()/2;
				score = 0;
				scoreString = String.valueOf(score);
				velocity = 0;
				coinXs.clear();
				coinYs.clear();
				coinRectangle.clear();
				coinCount = 0;

				bombXs.clear();
				bombYs.clear();
				bombRectangle.clear();
				bombCount = 0;
			}
		}else if(gameState == 3){

			if(Gdx.input.justTouched()){
				gameState =1;
				manY = Gdx.graphics.getHeight()/2;
				score = 0;
				scoreString = String.valueOf(score);
				velocity = 0;
				coinXs.clear();
				coinYs.clear();
				coinRectangle.clear();
				coinCount = 0;

				bombXs.clear();
				bombYs.clear();
				bombRectangle.clear();
				bombCount = 0;
			}
		}



		if(gameState == 2){
			batch.draw(dizzy,Gdx.graphics.getWidth()/2-man[manState].getWidth()/2,manY);
		}else if(gameState == 1||gameState == 0){
			batch.draw(man[manState],Gdx.graphics.getWidth()/2-man[manState].getWidth()/2,manY);
		}else if(gameState == 3){
			batch.draw(princess,200+50+win.getWidth()/2,250);
			batch.draw(win,200,250);

		}



		manRectangle = new Rectangle(Gdx.graphics.getWidth()/2-man[manState].getWidth()/2,manY,man[manState].getWidth(),man[manState].getHeight());

		for (int i = 0;i<coinRectangle.size();i++){
			if(Intersector.overlaps(manRectangle,coinRectangle.get(i))){
				score++;
				scoreString = String.valueOf(score);
				bgCount++;

				if(score >= 25){
					scoreString = "Win";
					gameState = 3;


				}


				coinRectangle.remove(i);
				coinXs.remove(i);
				coinYs.remove(i);
				break;
			}
		}

		for (int i = 0;i<cakeRectangle.size();i++){
			if(Intersector.overlaps(manRectangle,cakeRectangle.get(i))){
				score = score +10;
				scoreString = String.valueOf(score);
				bgCount++;

				if(score >= 25){
					scoreString = "Win";
					gameState = 3;


				}


				cakeRectangle.remove(i);
				cakeXs.remove(i);
				cakeYs.remove(i);
				break;
			}
		}

		for (int i = 0;i<bombRectangle.size();i++){
			if(Intersector.overlaps(manRectangle,bombRectangle.get(i))){
				gameState = 2;
			}
		}


		font.draw(batch,scoreString,100,200);


		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
