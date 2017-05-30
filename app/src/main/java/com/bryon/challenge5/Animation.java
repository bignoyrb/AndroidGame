// CS 2430 Challenge 5
// Bryon Reynolds
// ID: 0514518

package com.bryon.challenge5;

import android.graphics.Bitmap;

/**
 * Created by Bryon on 2016-03-23.
 */
public class Animation {

    private Bitmap[] frames;
    private int currentFrame;
    private long startTime;
    private long delay;
    private boolean playedOnce; // needed to play animation only once

    public void setFrames(Bitmap[] frames){

        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
    }

    public void setDelay(long delay){
        this.delay = delay;
    }

    public void setFrame(int i){

        currentFrame = i;
    }

    public void update(){

        long elapsed = (System.nanoTime() - startTime)/1000000;

        if(elapsed > delay){

            currentFrame++;
            startTime = System.nanoTime();
        }

        if(currentFrame == frames.length){

            currentFrame = 0;
            playedOnce = true;
        }
    }

    public Bitmap getImage(){

        return frames[currentFrame];
    }

    public boolean playedOnce(){

        return playedOnce;
    }
}
