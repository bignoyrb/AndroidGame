// CS 2430 Challenge 5
// Bryon Reynolds
// ID: 0514518


package com.bryon.challenge5;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Bryon on 2016-03-26.
 */
public class PlayerLaser extends GameItem {

    private Bitmap image;
    private int dx;
    private int speed = 10;

    public PlayerLaser (Bitmap img, int x, int y, int w, int h){

        this.x = x;
        this.y = y;
        width = w;
        height = h;
        image = img;
        dx = speed;
    }


    public void update(){

        x += dx;
    }

    public void draw(Canvas canvas){

        canvas.drawBitmap(image, x, y, null);

    }

    public int getX(){

        return x;
    }
}