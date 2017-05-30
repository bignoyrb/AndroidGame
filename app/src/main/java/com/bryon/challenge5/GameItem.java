// CS 2430 Challenge 5
// Bryon Reynolds
// ID: 0514518

package com.bryon.challenge5;

import android.graphics.Rect;

/**
 * Created by Bryon on 2016-03-23.
 */
public abstract class GameItem {

    protected int x;
    protected int y;
    protected int dy;
    protected int width;
    protected int height;

    public void setX(int x){

        this.x = x;
    }

    public void setY(int y){

        this.y = y;
    }

    public int getX(){

        return x;
    }

    public int getY(){

        return y;
    }

    public int getHeight(){

        return height;
    }

    public Rect getRectangle(){

        return new Rect(x, y, x + width, y + height);
    }

}
