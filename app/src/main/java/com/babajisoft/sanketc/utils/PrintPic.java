package com.babajisoft.sanketc.utils;

/**
 * Created by babaji on 18/9/16.
 */
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.v4.media.TransportMediator;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@SuppressLint({"SdCardPath"})
public class PrintPic {
    public byte[] bitbuf;
    public Bitmap bm;
    public Canvas canvas;
    public float length;
    public Paint paint;
    public int width;

    public PrintPic() {
        this.canvas = null;
        this.paint = null;
        this.bm = null;
        this.length = 0.0f;
        this.bitbuf = null;
    }

    public int getLength() {
        return ((int) this.length) + 20;
    }

    public void initCanvas(int w) {
        this.bm = Bitmap.createBitmap(w, w * 10, Config.ARGB_4444);
        this.canvas = new Canvas(this.bm);
        this.canvas.drawColor(-1);
        this.width = w;
        this.bitbuf = new byte[(this.width / 8)];
    }

    public void initPaint() {
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.paint.setStyle(Style.STROKE);
    }

    public void drawImage(float x, float y, String path) {
        try {
            Bitmap btm = BitmapFactory.decodeFile(path);
            this.canvas.drawBitmap(btm, x, y, null);
            if (this.length < ((float) btm.getHeight()) + y) {
                this.length = ((float) btm.getHeight()) + y;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printPng() {
        FileNotFoundException e;
        File f = new File("/mnt/sdcard/0.png");
        Bitmap nbm = Bitmap.createBitmap(this.bm, 0, 0, this.width, getLength());
        try {
            FileOutputStream fos = new FileOutputStream(f);
            FileOutputStream fileOutputStream;
           // try {
                nbm.compress(CompressFormat.PNG, 50, fos);
                fileOutputStream = fos;
           /* } catch (FileNotFoundException e2) {
                e = e2;
                fileOutputStream = fos;
                e.printStackTrace();
            }*/
        } catch (FileNotFoundException e3) {
            e = e3;
            e.printStackTrace();
        }
    }

    public byte[] printDraw() {
        Bitmap nbm = Bitmap.createBitmap(this.bm, 0, 0, this.width, getLength());
        byte[] imgbuf = new byte[(((this.width / 8) * getLength()) + 8)];
        imgbuf[0] = (byte) 29;
        imgbuf[1] = (byte) 118;
        imgbuf[2] = (byte) 48;
        imgbuf[3] = (byte) 0;
        imgbuf[4] = (byte) (this.width / 8);
        imgbuf[5] = (byte) 0;
        imgbuf[6] = (byte) (getLength() % AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY);
        imgbuf[7] = (byte) (getLength() / AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY);
        int s = 7;
        for (int i = 0; i < getLength(); i++) {
            int k = 0;
            while (true) {
                if (k >= this.width / 8) {
                    break;
                }
                int p0;
                int p1;
                int p2;
                int p3;
                int p4;
                int p5;
                int p6;
                int p7;
                if (nbm.getPixel((k * 8) + 0, i) == -1) {
                    p0 = 0;
                } else {
                    p0 = 1;
                }
                if (nbm.getPixel((k * 8) + 1, i) == -1) {
                    p1 = 0;
                } else {
                    p1 = 1;
                }
                if (nbm.getPixel((k * 8) + 2, i) == -1) {
                    p2 = 0;
                } else {
                    p2 = 1;
                }
                if (nbm.getPixel((k * 8) + 3, i) == -1) {
                    p3 = 0;
                } else {
                    p3 = 1;
                }
                if (nbm.getPixel((k * 8) + 4, i) == -1) {
                    p4 = 0;
                } else {
                    p4 = 1;
                }
                if (nbm.getPixel((k * 8) + 5, i) == -1) {
                    p5 = 0;
                } else {
                    p5 = 1;
                }
                if (nbm.getPixel((k * 8) + 6, i) == -1) {
                    p6 = 0;
                } else {
                    p6 = 1;
                }
                if (nbm.getPixel((k * 8) + 7, i) == -1) {
                    p7 = 0;
                } else {
                    p7 = 1;
                }
                int value = (((((((p0 * TransportMediator.FLAG_KEY_MEDIA_NEXT) + (p1 * 64)) + (p2 * 32)) + (p3 * 16)) + (p4 * 8)) + (p5 * 4)) + (p6 * 2)) + p7;
                this.bitbuf[k] = (byte) value;
                k++;
            }
            int t = 0;
            while (true) {
                if (t >= this.width / 8) {
                    break;
                }
                s++;
                imgbuf[s] = this.bitbuf[t];
                t++;
            }
        }
        return imgbuf;
    }
}