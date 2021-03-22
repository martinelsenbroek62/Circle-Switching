package com.example.welcome_smile.speed_in;

/**
 * Created by Welcome_Smile on 3/11/2018.
 */


import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by Welcome_Smile on 2/24/2018.
 */

public class HUD extends Service implements View.OnTouchListener {
    public static Button mButton;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        mButton = new Button(this);
        mButton.setBackgroundResource(R.drawable.btn);
        mButton.setOnTouchListener(this);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                70,
                70,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL ,
                PixelFormat.TRANSPARENT
        );
        //params.gravity = Gravity.RIGHT | Gravity.TOP;
        params.gravity = Gravity.CENTER;
        params.setTitle("Load Icons");
        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        wm.addView(mButton, params);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Intent i = new Intent();
        i.setClass(this, DrawAppIcon.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        mButton.setVisibility(View.GONE);
        return false;
    }

}