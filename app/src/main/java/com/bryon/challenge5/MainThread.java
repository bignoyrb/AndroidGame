// CS 2430 Challenge 5
// Bryon Reynolds
// ID: 0514518

package com.bryon.challenge5;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Bryon on 2016-03-22.
 */
public class MainThread extends Thread{

    private SurfaceHolder surfaceHolder;
    private GameScreen  gameScreen;
    private boolean isRunning;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GameScreen gameScreen){

        super();
        this.surfaceHolder = surfaceHolder;
        this.gameScreen = gameScreen;
    }

    @Override
    public void run() {

        while (isRunning) {

           canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                this.gameScreen.update();
                this.gameScreen.draw(canvas);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            finally {
                if (canvas != null){
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void setRunning(boolean bool){

        isRunning = bool;
    }
}
