// CS 2430 Challenge 5
// Bryon Reynolds
// ID: 0514518


package com.bryon.challenge5;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Bryon on 2016-03-27.
 */
public class Blast  {
    private int x;
    private int y;
    private Animation animation = new Animation();
    private Bitmap spritesheet;
    private int height;
    private int width;

    public Blast(Bitmap res, int x, int y, int width, int height, int numFrames) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        for (int i = 0; i < image.length; i++){

            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(10);
    }
    public void draw(Canvas canvas){

        // omly update if animation hasn't completed
       if(!animation.playedOnce()){

            canvas.drawBitmap(animation.getImage(),x,y,null);
       }
    }
    public void update(){

        // omly update if animation hasn't completed
        if(!animation.playedOnce()){

            animation.update();
        }
    }
    public boolean getPlayed(){

        return animation.playedOnce();
    }
}
