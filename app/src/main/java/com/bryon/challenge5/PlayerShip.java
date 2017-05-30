// CS 2430 Challenge 5
// Bryon Reynolds
// ID: 0514518

package com.bryon.challenge5;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Bryon on 2016-03-23.
 */
public class PlayerShip extends GameItem{
    private Bitmap spritesheet;
    private double dya;
    private boolean up;
    private boolean playing;
    private Animation animation = new Animation();

    public PlayerShip (Bitmap res, int w, int h, int numFrames){

        x = 100;
        y = 950;
        dy = 0;
        height = h;
        width = w;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i < image.length; i++){

            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(10);
    }

    public void setUp(boolean bool){

        up = bool;
    }

    public void update(){

        animation.update();

        if(up){

            dy = (int)(dya-=1.1);
        }
        else{

            dy = (int)(dya+=1.1);
        }

        if(dy > 10){

            dy = 10;
        }

        if(dy < -10){

            dy = -10;
        }

        y += dy*2;
        dy = 0;

        if( y >= 950  ){

            y = 950;
            dya = 0;
        }

        if( y <= 10  ){

            y = 10;
            dya = 0;
        }
    }

    public void draw(Canvas canvas){

        canvas.drawBitmap(animation.getImage(), x, y, null);
    }

   public boolean getPlaying(){

       return playing;
    }

    public void setPlaying(boolean bool){

        playing = bool;
    }

    public int getY(){

        return y;
    }
}
