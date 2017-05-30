// CS 2430 Challenge 5
// Bryon Reynolds
// ID: 0514518


package com.bryon.challenge5;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by Bryon on 2016-03-26.
 */
public class EnemyShip extends GameItem {

    private Random rand = new Random();
    private Animation animation = new Animation();
    private Bitmap spritesheet;
    private int speed;

    public EnemyShip(Bitmap res, int x, int y, int w, int h,  int numFrames){

        this.x = x;
        this.y = y;
        width = w;
        height = h;
        speed = 5;

        // cap enemy speed
        if (speed > 50){

            speed = 50;
        }

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        for (int i = 0; i < image.length; i++){

            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(10);
    }

    public void update(){

        x -= speed;
        animation.update();
    }

    public void draw(Canvas canvas){

        canvas.drawBitmap(animation.getImage(), x, y, null);
    }
}
