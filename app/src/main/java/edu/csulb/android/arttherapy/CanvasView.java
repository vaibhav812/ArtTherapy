package edu.csulb.android.arttherapy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by vaibhavjain on 3/16/2017.
 */

public class CanvasView extends View {

    private Path mPath;
    private Canvas mCanvas;
    private Paint mPaint;
    private Bitmap mBitmap;
    private boolean isErasing;
    private MediaPlayer player;
    private final PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    public CanvasView(Context ctx, AttributeSet attr){
        super(ctx, attr);
        init(ctx);
    }

    private void init(Context ctx){
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
        isErasing = false;
        player = MediaPlayer.create(ctx, R.raw.eraser_music);
        player.setLooping(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isErasing){
            mPaint.setXfermode(mode);
        }
        else{
            mPaint.setXfermode(null);
            mPaint.setColor(Color.BLACK);
        }
        canvas.drawBitmap(mBitmap, 0 ,0 , mPaint);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xCoord = event.getX();
        float yCoord = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(xCoord, yCoord);
                if(isErasing && player != null) {
                    AsyncTask playMusicTask = new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object[] params) {
                            player.start();
                            return null;
                        }
                    };
                    playMusicTask.execute();
                }
                break;

            case MotionEvent.ACTION_UP:
                mCanvas.drawPath(mPath, mPaint);
                mPath.reset();
                if(isErasing && player != null && player.isPlaying()) {
                    player.pause();
                }
                break;

            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(xCoord, yCoord);
                break;
        }
        invalidate();
        return true;
    }

    public void setErasing(boolean isErasing){
        this.isErasing = isErasing;
    }

    public void reset(boolean shakeEvent){
        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        if(shakeEvent) {
            AsyncTask playMusicTask = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    player.start();
                    return null;
                }
            };
            playMusicTask.execute();
        }
        invalidate();
    }

    public void releaseMediaPlayer(){
        if(player != null){
            player.release();
            player = null;
        }
    }
}
