package com.example.welcome_smile.speed_in;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import static com.example.welcome_smile.speed_in.DrawAppIcon.x;
import static com.example.welcome_smile.speed_in.DrawAppIcon.y;

/**
 * Created by Welcome_Smile on 3/12/2018.
 */

public class DrawAlpha extends View {

    private  Paint paint;
    float x = 0;
    float y = 0;
    float x1 = 0;
    float y1 = 0;
    public DrawAlpha(Context context,float xprime, float yprime,  float x1prime, float y1prime) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        x = xprime;
        y = yprime;
        x1 = x1prime;
        y1 = y1prime;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {


        canvas.drawLine(DrawAppIcon.x, DrawAppIcon.y, x1, y1 ,paint);
        super.onDraw(canvas);

    }
}
