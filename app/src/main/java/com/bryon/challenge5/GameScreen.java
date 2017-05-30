// CS 2430 Challenge 5
// Bryon Reynolds
// ID: 0514518

package com.bryon.challenge5;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Bryon on 2016-03-22.
 */
public class GameScreen extends SurfaceView implements SurfaceHolder.Callback{

    private MainThread thread;

    private Background bGround;
    private PlayerShip player;

    private SoundPool sounds;
    private int soundLaser;
    private int soundEx;
    private int soundGameOver;

    private ArrayList<PlayerLaser> laser;
    private long laserStartTime;

    private ArrayList<EnemyShip> enemy;
    private long enemyStartTime;

    private ArrayList<Blast> blast;

    private boolean playerHit = false;
    private boolean firstTime = true;

    private Random rand = new Random();
    private int score = 0;
    private int highScore = 0;

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;

    public GameScreen(Context context){

        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    // when the surface is destroyed kill the thread
    @Override
    public void surfaceDestroyed(SurfaceHolder holder){

        boolean retry = true;
        int count = 0;

        while(retry && count < 10000){

            try{
                count++;
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;
            }
            catch(InterruptedException e){

                e.printStackTrace();
            }
        }
    }

    // create all the items when the surface is created and start the thread
    @Override
    public void surfaceCreated(SurfaceHolder holder){

        thread = new MainThread(getHolder(),this);
        thread.setRunning(true);
        thread.start();

        bGround = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.spacebackground));
        player = new PlayerShip(BitmapFactory.decodeResource(getResources(), R.drawable.playersheet), 219, 82, 5);
        enemy = new ArrayList<EnemyShip>();
        enemyStartTime = System.nanoTime();
        laser = new ArrayList<PlayerLaser>();
        laserStartTime = System.nanoTime();
        blast = new ArrayList<Blast>();

        sounds = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        soundLaser = sounds.load(getContext(), R.raw.laser, 1);
        soundEx = sounds.load(getContext(), R.raw.explosion, 1);
        soundGameOver = sounds.load(getContext(), R.raw.gameover, 1);

    }

    @Override
    public boolean onTouchEvent (MotionEvent event){

        if(event.getAction() == MotionEvent.ACTION_DOWN){

            // if player isn't playing touch start gane
            if( !player.getPlaying() ){

                player.setPlaying(true);
                player.x = 100;
                firstTime = false;
            }

            else {
                // if player is playing touch make him go up
                player.setUp(true);
            }

            return true;
        }
        // set up to fale so player goes down
        if(event.getAction() == MotionEvent.ACTION_UP){

           player.setUp(false);
           return true;
        }

        return super.onTouchEvent(event);
    }

    // This function updates the game items
    public void update() {

        // only update if the player is playing
        if (player.getPlaying()) {

            // update the player and background
            player.update();
            bGround.update();

            // if the player lands on the planet stop moving the background, if he takes off start again
            if (player.getY() >= 950) {

                bGround.setdx(0);
            }
            else {

                bGround.setdx(-5);
            }

            // add enemy ship on a timer
            long enemyElapsed = (System.nanoTime() - enemyStartTime) / 1000000;

            if (enemyElapsed > 1500) {

                enemy.add(new EnemyShip(BitmapFactory.decodeResource(getResources(), R.drawable.enemyshipsheet), WIDTH + 20, (int) (rand.nextDouble() * (HEIGHT - 100)), 100, 69,  5));
                enemyStartTime = System.nanoTime();
            }

            // shoot a lazer based on a timer
            long laserElapsed = (System.nanoTime() - laserStartTime) / 1000000;

            if ((laserElapsed > 1000) && (player.y < 950)) {

                laser.add(new PlayerLaser(BitmapFactory.decodeResource(getResources(), R.drawable.playerlaser), player.x + 180, player.y + 65, 10, 61));
                laserStartTime = System.nanoTime();
                sounds.play(soundLaser, 1.0f, 1.0f,0,0,1.5f);
            }

            // update all the enemies
            for (int i = 0; i < enemy.size(); i++) {

                enemy.get(i).update();

                // if the enemy/player collide
                if (collision(enemy.get(i), player)) {

                    // create a blast animation and add it to the blast arraylist and play blast sound
                    blast.add(new Blast(BitmapFactory.decodeResource(getResources(), R.drawable.explosionsheet), player.getX() + (player.height / 2), player.getY(), 120, 112, 13));
                    sounds.play(soundGameOver, 1.0f, 1.0f, 0, 0, 1.5f);

                    player.x = -500;
                    playerHit = true;
                    clearScreen();

                    // set new highscore if current score is higher
                    if( score > highScore){

                        highScore = score;
                    }
                    score = 0; // reset score to 0
                    break;
                }

                // update the lasers
                for (int j = 0; j < laser.size(); j++) {

                    laser.get(j).update();

                    if(laser.get(j).getX() < 90){

                        laser.remove(j);
                        break;
                    }

                    if (collision(enemy.get(i),laser.get(j))) {

                        laser.remove(j);
                        sounds.play(soundEx, 1.0f, 1.0f, 0, 0, 1.5f);
                        blast.add(new Blast(BitmapFactory.decodeResource(getResources(), R.drawable.explosionsheet), enemy.get(i).getX() + (enemy.get(i).height / 2), enemy.get(i).getY(), 120, 112, 13));
                        enemy.remove(i);
                        score = score + 10;
                        break;
                    }
                }
            }

            for (int i = 0; i < blast.size(); i++) {

                blast.get(i).update();

                if (blast.get(i).getPlayed()){

                    blast.remove(i);
                }
            }

            if (blast.size() < 1 && playerHit ) {

                player.y = 950;
                player.setPlaying(false);
                playerHit = false;
            }

            clearStuffOffScreen();
        }
    }

    // this function draws everything on screen it also scales it
    @Override
    public void draw(Canvas canvas){

        // determine scale factors
        final float scaleFactorX = getWidth() / (WIDTH*1.f);
        final float scaleFactorY = getHeight() / (HEIGHT*1.f);

        if(canvas != null) {

            // save the canvas fur reset
            final int savedState = canvas.save();
            // scale canvas size
            canvas.scale(scaleFactorX, scaleFactorY);

            bGround.draw(canvas);
            player.draw(canvas);

            for (EnemyShip e: enemy) {
                e.draw(canvas);
            }

            for (PlayerLaser pl: laser) {
                pl.draw(canvas);
            }

            for (Blast bl: blast){
                bl.draw(canvas);
            }

            textStuff(canvas);

           // reset canvas, if you don't it will keep scaling
            canvas.restoreToCount( savedState );
        }
    }

    // this function clears any enemt and laser that is off the screen
    public void clearStuffOffScreen(){

        for (int i = 0; i < enemy.size(); i++) {

            enemy.get(i).update();

            // if and enemy is behind the player -5 from the score
            if (enemy.get(i).getX() < -100) {

                enemy.remove(i);
                score = score - 5;

                // make sure score is never negative
                if (score < 0){
                    score = 0;
                }
            }
        }

        for (int j = 0; j < laser.size(); j++) {

            laser.get(j).update();

            if (laser.get(j).getX() > GameScreen.WIDTH + 200) {
                laser.remove(j);
            }
        }
    }

    // this function test if two objects collide
    public boolean collision(GameItem item1, GameItem item2){

        if(Rect.intersects(item1.getRectangle(), item2.getRectangle())){
            return true;
        }
        return false;
    }

    // this function clears/removes all the enemies and lasers off the screen
    public void clearScreen(){

        enemy.clear();
        laser.clear();
    }

    // this functions draws all the text on the screen
    public void textStuff(Canvas canvas){

        // draw score and high score
        Paint textScore = new Paint();
        textScore.setColor(Color.WHITE);
        textScore.setTextSize(40);
        textScore.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("SCORE: " + score, 30, 50, textScore);
        canvas.drawText("HIGH SCORE: " + highScore, WIDTH - 350, 50, textScore);

        // draw my info on bottom corner
        Paint textName = new Paint();
        textName.setColor(Color.BLACK);
        textName.setTextSize(20);
        textName.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("BRYON REYNOLDS, 0514518", WIDTH - 330, HEIGHT - 30, textName);

        // if its the players first time starting the game draw this
        if(!player.getPlaying() && firstTime){

            Paint textStart = new Paint();
            textStart.setColor(Color.WHITE);
            textStart.setTextSize(100);
            textStart.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("TAP TO START GAME", 200, HEIGHT / 2, textStart);

            textStart.setTextSize(40);
            canvas.drawText("TAP AND HOLD TO GO UP ,  RELEASE TO GO DOWN", 200, HEIGHT/2 + 50, textStart);
            canvas.drawText("YOU GET 10 PTS PER ENEMY KILL", 200, HEIGHT / 2 + 100, textStart);
            canvas.drawText("YOU LOSE 5 PTS IF A ENEMY GET BY YOU", 200, HEIGHT/2 + 150, textStart);
        }

        // if its not the players first time starting the game draw this
        if(!player.getPlaying() && !firstTime){

            Paint textGameOver = new Paint();
            textGameOver.setColor(Color.WHITE);
            textGameOver.setTextSize(100);
            textGameOver.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("GAME OVER", 200, HEIGHT / 2, textGameOver);

            textGameOver.setTextSize(40);
            canvas.drawText("TAP TO PLAY AGAIN", 200, HEIGHT/2 + 50, textGameOver);
        }
    }
}
