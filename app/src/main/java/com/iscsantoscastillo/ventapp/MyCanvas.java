package com.iscsantoscastillo.ventapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MyCanvas extends View {
    Paint drawPaint, canvasPaint;
    Path drawPath;
    public Canvas drawCanvas;
    public Bitmap canvasBitmap;
    public MyCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawPaint = new Paint();
        drawPath = new Path();
        drawPaint.setAntiAlias(true);
        drawPaint.setColor(Color.BLACK);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        drawPaint.setStrokeWidth(10f);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    public void guardarImagen(){
        Bitmap b = getCanvasBitmap();
        //create directory if not exist
        File dir = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "PDF" + File.separator);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File output = new File(dir, "firma.png");
        OutputStream os = null;
        try {
            os = new FileOutputStream(output);
            b.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
        }
    }

    public Bitmap getCanvasBitmap(){
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);
        return bmp;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas){
        //super.onDraw(canvas);
        //canvas.drawPath(drawPath, paint);
        canvas.drawBitmap(canvasBitmap,0,0, drawPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xPos = event.getX();
        float yPos = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            {
                drawPath.moveTo(xPos, yPos);
                return true;
            }

            case MotionEvent.ACTION_MOVE:
            {
                drawPath.lineTo(xPos, yPos);
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                drawPath.lineTo(xPos, yPos);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            }
            default: return false;

        }
        invalidate();
        return true;
    }

    public void limpiarCanvas(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }
}
