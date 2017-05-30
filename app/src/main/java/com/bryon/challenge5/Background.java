// CS 2430 Challenge 5
// Bryon Reynolds
// ID: 0514518

package com.bryon.challenge5;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Bryon on 2016-03-22.
 */
public class Background {

    private Bitmap image;
    private int x;
    private int y;
    private int dx;

    public Background (Bitmap img){

        image = img;
        dx = -5;
    }

    public void setdx ( int speed){

        dx = speed;
    }

    public void update(){

        x += dx;
        if (x < -GameScreen.WIDTH) {

            x = 0;
        }
    }

    public void draw(Canvas canvas){

        canvas.drawBitmap(image, x, y, null);
        if(x < 0){

            canvas.drawBitmap(image, x + GameScreen.WIDTH, y, null);
        }
    }

}
