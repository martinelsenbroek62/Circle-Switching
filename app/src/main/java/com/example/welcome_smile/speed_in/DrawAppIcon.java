package com.example.welcome_smile.speed_in;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class DrawAppIcon extends Activity {

    public List<PackageInfo> packagelist;
    ImageView[] arrayOfImages;



    DrawAlpha drawAlpha;
    PackageManager packageManager;
    RelativeLayout nn;

    ImageView innerCircle;
    Button newBtn;


    double pi = 3.14;


    //screen size
    int s_width = 0;
    int s_height = 0;


    //number of apps
    int launchable_apps = 0;
    int appPerPage = 20;

    List<Integer> indexOfPac = new ArrayList<Integer>();
    List<Integer> indexOfFirstApp = new ArrayList<Integer>();
    // number of different alphabetics
    int count_alpha = 0;

    //number of page
    int maxPage = 0;
    int realAppPerPage = 0;

    int indexangle = 0;
    int cursor_index;

    //number of next button click times
    int btnclk = 0;

    //outer circle (app icon circle)
    public static float r = 0;
    public static float x = 0;
    public static float y = 0;

    public static float[] x1;
    public static float[] y1;


    //time counting for closing app
    long tstart = 0;
    long tend = 0;
    long tdelta = 0;


    //app icon size
    final int baseIcon_Width = 70;
    final int baseIcon_Height = 70;

    int selIcon_Width = 90;
    int selIcon_Height = 90;

    final int dTouchSel_Wid = 130;
    final int dtouchSel_Hei = 130;

    String[] appName;
    //flags

    boolean clsFlag;
    boolean innerFlag;
    boolean midFlag;
    boolean dtouchFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_app_icon);

        //formatting all component
        packageManager = getPackageManager();
        nn = findViewById(R.id.imgID);
        innerCircle = findViewById(R.id.inner_circle);

        //Getting screen width and height
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        s_width = size.x;
        s_height = size.y;
        cursor_index = -1;

        //Sort alphabetically
        final PackageItemInfo.DisplayNameComparator comparator = new PackageItemInfo.DisplayNameComparator(packageManager);
        packagelist = getPackageManager().getInstalledPackages(0);

        Collections.sort(packagelist, new Comparator<PackageInfo>() {
            @Override
            public int compare(PackageInfo lhs, PackageInfo rhs) {
                return comparator.compare(lhs.applicationInfo, rhs.applicationInfo);
            }
        });

        //Find only launchable app package name and app name
        for (int i = 0; i < packagelist.size(); i++) {
            PackageInfo packageInfo = packagelist.get(i);
            try {
                ApplicationInfo appInfo = getPackageManager().getApplicationInfo(packageInfo.packageName, 0);

                if (packageManager.getLaunchIntentForPackage(appInfo.packageName) != null) {
                    // String appname = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                    launchable_apps++;
                    indexOfPac.add(i);

                    //Log.e("id", "$$$$$$$$$$ " + i);

                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }


        // app array formatting

        arrayOfImages = new ImageView[launchable_apps];

        // Getting max page
        if (launchable_apps % appPerPage > 0) maxPage = launchable_apps / appPerPage + 1;
        else if (launchable_apps % appPerPage == 0) maxPage = launchable_apps / appPerPage;


        //Getting center of app icon circle
        if (s_height > s_width) {
            r = s_width / 2 - 150;
            x = s_width / 2 - 30;
            y = s_height / 2 - 50;
        } else {
            r = s_height / 2 - 190;
            x = s_width / 2 - 30;
            y = s_height / 2 - 50;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        tstart = System.currentTimeMillis();

        //Draw app icon
        if (launchable_apps > appPerPage) {
            drawAnimationCircle(0, appPerPage - 1, 0);

            realAppPerPage = appPerPage;
        } else {
            drawAnimationCircle(0, launchable_apps - 1, 0);

            realAppPerPage = launchable_apps;
        }

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tend = System.currentTimeMillis();
                if (clsFlag == true) {
                    tstart = System.currentTimeMillis();
                    clsFlag = false;
                }

                if (tend - tstart > 3000 && clsFlag == false) {

                  /*  //innerCircle.setVisibility(View.GONE);
                    //newBtn.setVisibility(View.GONE);
                    Log.e("Min Max","Min>>>>>>"+min + "Max>>>>>>"+max);
                   /*//* arrayOfImages[5].setVisibility(View.GONE);
                    arrayOfImages[6].setVisibility(View.GONE);
                    arrayOfImages[7].setVisibility(View.GONE);*//**//*
                   *//* for (int i = min; i<max; i++)
                    {
                        arrayOfImages[i].setVisibility(View.GONE);
                    }*//**/

                    HUD.mButton.setVisibility(View.VISIBLE);
                    finish();
                    //Log.e("AAAAAAAA", "asjdlajsdlkajldsjldajsldajldajl");
                } else handler.postDelayed(this, 1000);

            }
        };
        handler.post(runnable);

        newBtn = findViewById(R.id.newBtn);
        newBtn.setBackgroundResource(R.drawable.btn);
        newBtn.setVisibility(View.VISIBLE);
        newBtn.setX((x - Math.round(r / Math.sqrt(2)) + 95) + (Math.round(r * Math.sqrt(2)) - 120) / 2 - 35);
        newBtn.setY((y - Math.round(r / Math.sqrt(2)) + 95) + (Math.round(r * Math.sqrt(2)) - 120) / 2 - 40);

        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clsFlag = true;
                btnclk++;

                if (btnclk == maxPage) btnclk = 0;

                if (btnclk * appPerPage + appPerPage > launchable_apps) {
                    drawAnimationCircle(btnclk * appPerPage, launchable_apps - 1, 0);
                    realAppPerPage = launchable_apps - btnclk * appPerPage;

                } else {
                    drawAnimationCircle(btnclk * appPerPage, btnclk * appPerPage + appPerPage - 1, 0);
                    realAppPerPage = appPerPage;
                }
            }
        });

        //Move finger inside the middle circle
        nn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float rcur_x;
                float rcur_y;
                float rold_x;
                float rold_y;
                float roangle = 0;
                float rcangle = 0;
                float rdangle = 0;


                int ref_x = innerCircle.getWidth() / 2;
                int ref_y = innerCircle.getHeight() / 2;

                int x1 = (int) (ref_x + (x - Math.round(r / Math.sqrt(2)) + 70));
                int y1 = (int) (ref_y + (y - Math.round(r / Math.sqrt(2)) + 70));

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    clsFlag = true;
                    rold_x = event.getX();
                    rold_y = event.getY();
                    if (Math.sqrt(Math.pow((rold_x - x1), 2) + Math.pow((rold_y - y1), 2)) > r) {
                        midFlag = false;
                        x = (int) rold_x;
                        y = (int) rold_y;
                        if (btnclk * appPerPage + appPerPage > launchable_apps) {
                            drawCircle(btnclk * appPerPage, launchable_apps - 1, 0);
                            realAppPerPage = launchable_apps - btnclk * appPerPage;

                            newBtn.setX((x - Math.round(r / Math.sqrt(2)) + 70) + (Math.round(r * Math.sqrt(2)) - 80) / 2 - 35);
                            newBtn.setY((y - Math.round(r / Math.sqrt(2)) + 70) + (Math.round(r * Math.sqrt(2)) - 80) / 2 - 40);
                        } else {
                            drawCircle(btnclk * appPerPage, btnclk * appPerPage + appPerPage - 1, 0);
                            realAppPerPage = appPerPage;
                            newBtn.setX((x - Math.round(r / Math.sqrt(2)) + 70) + (Math.round(r * Math.sqrt(2)) - 80) / 2 - 35);
                            newBtn.setY((y - Math.round(r / Math.sqrt(2)) + 70) + (Math.round(r * Math.sqrt(2)) - 80) / 2 - 40);
                        }
                    } else if (Math.sqrt(Math.pow((rold_x - x1), 2) + Math.pow((rold_y - y1), 2)) > ((int) Math.round(r * Math.sqrt(2)) - 80) / 2 && Math.sqrt(Math.pow((rold_x - x1), 2) + Math.pow((rold_y - y1), 2)) < r)
                        midFlag = true;
                    else midFlag = false;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    clsFlag = true;
                    rcur_x = event.getX();
                    rcur_y = event.getY();
                    if (midFlag == true) {
                        rcangle = (float) Math.atan((double) ((rcur_x - x) / (y - rcur_y)));
                        if (y - rcur_y < 0) rcangle += pi;
                        else if (rcur_x - x < 0) rcangle += pi * 2;
                        //     Log.e("RCANGLE", "//////////////************" + rcangle);
                        rdangle = rcangle - roangle;

                        if (btnclk * appPerPage + appPerPage > launchable_apps) {
                            drawCircle(btnclk * appPerPage, launchable_apps - 1, rdangle);
                            realAppPerPage = launchable_apps - btnclk * appPerPage;
                        } else {
                            drawCircle(btnclk * appPerPage, btnclk * appPerPage + appPerPage - 1, rdangle);
                            realAppPerPage = appPerPage;
                        }
                    } else {
                        x = (int) rcur_x;
                        y = (int) rcur_y;
                        if (btnclk * appPerPage + appPerPage > launchable_apps) {
                            drawCircle(btnclk * appPerPage, launchable_apps - 1, rdangle);
                            realAppPerPage = launchable_apps - btnclk * appPerPage;
                            newBtn.setX((x - Math.round(r / Math.sqrt(2)) + 70) + (Math.round(r * Math.sqrt(2)) - 80) / 2 - 35);
                            newBtn.setY((y - Math.round(r / Math.sqrt(2)) + 70) + (Math.round(r * Math.sqrt(2)) - 80) / 2 - 40);
                        } else {
                            drawCircle(btnclk * appPerPage, btnclk * appPerPage + appPerPage - 1, rdangle);
                            realAppPerPage = appPerPage;
                            newBtn.setX((x - Math.round(r / Math.sqrt(2)) + 70) + (Math.round(r * Math.sqrt(2)) - 80) / 2 - 35);
                            newBtn.setY((y - Math.round(r / Math.sqrt(2)) + 70) + (Math.round(r * Math.sqrt(2)) - 80) / 2 - 40);
                        }

                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    clsFlag = true;
                    rcur_x = event.getX();
                    rcur_y = event.getY();
                    rcangle = (float) Math.atan((double) ((rcur_x - x) / (y - rcur_y)));
                    if (midFlag == true) {
                        if (y - rcur_y < 0) {
                            rcangle += pi;
                            indexangle = (int) Math.round(rcangle / (2 * pi / realAppPerPage));
                        } else if (rcur_x - x < 0) {
                            rcangle += pi * 2;
                            indexangle = (int) Math.round(rcangle / (2 * pi / realAppPerPage));
                        }
                    } else
                        indexangle = 0;
                    rdangle = rcangle - roangle;
                    //Log.e("ANGLE", "" + 180 / pi * rdangle);
                    //Log.e("index angle", "" + indexangle );
                }
                return true;
            }

        });

        innerCircle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int ref_x = 0;
                int ref_y = 0;
                float cur_x = 0;
                float cur_y = 0;
                float angle = 0;
                int index = 0;


                ref_x = innerCircle.getWidth() / 2;
                ref_y = innerCircle.getHeight() / 2;

                //Log.e("outer circle : inner", "" + x + "____" + ref_x);
                cur_x = event.getX();
                cur_y = event.getY();
                angle = (float) Math.atan((double) ((cur_x - ref_x) / (ref_y - cur_y)));
                if (ref_y - cur_y < 0) angle += pi;
                else if (cur_x - ref_x < 0) angle += pi * 2;

                // Log.e("12345", "%%%%%%%%%%%%%%%" + angle);
                index = (int) Math.round(angle / (2 * pi / realAppPerPage));
                if (index == realAppPerPage) index = 0;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    clsFlag = true;

                    /*Log.e("index of index", "^^^^^^^^^^^^^" + indexangle);
                    Log.e("index ", "^^^^^^^^^^^^^^^^^^^^^^^" + index);
                    */
                    try {
                        arrayOfImages[btnclk * appPerPage + ((realAppPerPage + index - indexangle) % realAppPerPage)].requestLayout();
                        arrayOfImages[btnclk * appPerPage + ((realAppPerPage + index - indexangle) % realAppPerPage)].getLayoutParams().width = baseIcon_Width + 60;
                        arrayOfImages[btnclk * appPerPage + ((realAppPerPage + index - indexangle) % realAppPerPage)].getLayoutParams().height = baseIcon_Height + 60;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                    //  Log.e("current position", String.valueOf((btnclk) * appPerPage + index));
                    //Toast.makeText(getApplicationContext(), (int)cur_x, Toast.LENGTH_LONG).show();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    clsFlag = true;

                    if (Math.sqrt(Math.pow(cur_x - ref_x, 2) + Math.pow(cur_y - ref_y, 2)) > ((int) Math.round(r * Math.sqrt(2)) - 80) / 2)
                        innerFlag = false;
                    else innerFlag = true;


                    // Log.e("++++++++++++", innerFlag + " :: index : " + index);
                    if (innerFlag == true) {
                        if (cursor_index > -1) {
                            if (cursor_index != btnclk * appPerPage + ((realAppPerPage + index - indexangle) % realAppPerPage)) {
                                // Log.e("Cursor_index", "8888888888888888888888888" + cursor_index);
                                arrayOfImages[cursor_index].requestLayout();
                                arrayOfImages[cursor_index].getLayoutParams().width = baseIcon_Width;
                                arrayOfImages[cursor_index].getLayoutParams().height = baseIcon_Height;
                                // Log.e("testing4","----------+" + cursor_index);
                                cursor_index = btnclk * appPerPage + ((realAppPerPage + index - indexangle) % realAppPerPage);
                                arrayOfImages[cursor_index].requestLayout();
                                arrayOfImages[cursor_index].getLayoutParams().width = baseIcon_Width + 60;
                                arrayOfImages[cursor_index].getLayoutParams().height = baseIcon_Height + 60;
                                // Log.e("testing1","++++++++: "+cursor_index);
                            }
                            // Log.e("testing3","----------=" + cursor_index);
                        } else {
//                            if(midFlag==false) indexangle =0;
                            cursor_index = btnclk * appPerPage + ((realAppPerPage + index - indexangle) % realAppPerPage);
                            arrayOfImages[cursor_index].requestLayout();
                            arrayOfImages[cursor_index].getLayoutParams().width = baseIcon_Width + selIcon_Width;
                            arrayOfImages[cursor_index].getLayoutParams().height = baseIcon_Height + selIcon_Height;
                            //   Log.e("currentindex","+##################333333333333333+++++++: "+cursor_index);
                            // Log.e("realAppPerPage","+##################333333333333333+++++++: "+realAppPerPage);
                            // Log.e("index","+##################333333333333333+++++++: "+index);
                            // Log.e("indexangle","+##################333333333333333+++++++: "+indexangle);


                        }
                    } else {
                        try {
                            arrayOfImages[cursor_index].requestLayout();
                            arrayOfImages[cursor_index].getLayoutParams().width = baseIcon_Width;
                            arrayOfImages[cursor_index].getLayoutParams().height = baseIcon_Height;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    }
//                    Log.e("current position", "cur_x : " + cur_x + ":  cur_y : " + cur_y + " ori_x : " + ref_x + "  ori_y : " + ref_y);
                    //Toast.makeText(getApplicationContext(), (int)cur_x, Toast.LENGTH_LONG).show();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    clsFlag = true;
                    //HUD.mButton.setVisibility(View.VISIBLE);
                    if (innerFlag == true) {
                        HUD.mButton.setVisibility(View.VISIBLE);
                        cursor_index = -1;

                        // Log.e("testing3", "----------=" + cursor_index);
                        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(packagelist.get(indexOfPac.get(btnclk * appPerPage + ((realAppPerPage + index - indexangle) % realAppPerPage))).packageName);
                        startActivity(LaunchIntent);
                    }

//                    Log.e("current position", cur_x + "/////" + cur_y);
                    //Toast.makeText(getApplicationContext(), (int)cur_x, Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });


    }

    private int i;

    public void drawAnimationCircle(int min, int max, final float delta_angle) {

        nn.removeAllViews();
        final int upperLim = max;
        final int lowerLim = min;
        final String[] appName = new String[max - min + 1];
        i = min;
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (i >= lowerLim && i <= upperLim) {

                    PackageInfo packageInfo = packagelist.get(indexOfPac.get(i));
                    final String pacName = packageInfo.packageName;
                    //appName[i]= packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();

                    try {
                        ApplicationInfo appInfo = getPackageManager().getApplicationInfo(packageInfo.packageName, 0);
                        if (packageManager.getLaunchIntentForPackage(appInfo.packageName) != null) {
                            Drawable appicon = null;

                            try {
                                appicon = getPackageManager().getApplicationIcon(pacName);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }

                            arrayOfImages[i] = new ImageView(getApplicationContext());
                            arrayOfImages[i].setImageDrawable(appicon);
                            arrayOfImages[i].setId(i);

                            //Circular imageview for app icon
                            Bitmap bitmap = ((BitmapDrawable) arrayOfImages[i].getDrawable()).getBitmap();
                            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(DrawAppIcon.this.getResources(), bitmap);
                            roundedBitmapDrawable.setCircular(true);
                            arrayOfImages[i].setImageDrawable(roundedBitmapDrawable);

                            arrayOfImages[i].setX(x + (float) Math.sin(2 * pi * (i - lowerLim) / (upperLim - lowerLim + 1) + delta_angle) * (float) r);
                            arrayOfImages[i].setY(y - (float) Math.cos(2 * pi * (i - lowerLim) / (upperLim - lowerLim + 1) + delta_angle) * (float) r);


                            //getting alphabetic first letter x, y


                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(baseIcon_Width, baseIcon_Height);
                            arrayOfImages[i].setLayoutParams(params);
                            // arrayOfImages[i].setVisibility(View.GONE);

                            nn.addView(arrayOfImages[i]);

                            final int finalI = i;
                            arrayOfImages[i].setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                        arrayOfImages[finalI].requestLayout();
                                        arrayOfImages[finalI].getLayoutParams().width = baseIcon_Width + dTouchSel_Wid;
                                        arrayOfImages[finalI].getLayoutParams().height = baseIcon_Height + dtouchSel_Hei;
                                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                                        clsFlag = true;
                                        HUD.mButton.setVisibility(View.VISIBLE);
                                        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(pacName);
                                        startActivity(LaunchIntent);

                                    }
                                    return true;
                                }
                            });

                            //draw inner circle
                            innerCircle.setX(x - Math.round(r / Math.sqrt(2)) + 95);
                            innerCircle.setY(y - Math.round(r / Math.sqrt(2)) + 95);

                            android.view.ViewGroup.LayoutParams layoutParams = innerCircle.getLayoutParams();
                            layoutParams.width = (int) Math.round(r * Math.sqrt(2)) - 120;
                            layoutParams.height = (int) Math.round(r * Math.sqrt(2)) - 120;
                            innerCircle.setLayoutParams(layoutParams);

                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    i++;
                    handler.postDelayed(this, 2);
                }
//
//                indexOfFirstApp.add(indexOfPac.get(lowerLim));
//                if(i >= lowerLim+1 && i<= upperLim-1)
//                {
//                    if(appName[i-1].toUpperCase().charAt(0)!= appName[i].toUpperCase().charAt(0)) {
////                        indexOfFirstApp.add(indexOfPac.get(i));
//                        indexOfFirstApp.add(i);
//                        drawAlpha = new DrawAlpha(DrawAppIcon.this, arrayOfImages[indexOfFirstApp.get(i)].getX(), arrayOfImages[indexOfFirstApp.get(i)].getY());
////                        drawAlpha.draw();
//
//                    }
//                    i++;
//
//                }
            }
        };
        handler.post(runnable);
    }

    public void drawCircle(int min, int max, float delta_angle) {


        nn.removeAllViews();
        appName = new String[max - min + 1];

        for (int i = min; i <= max; i++) {
            PackageInfo packageInfo = packagelist.get(indexOfPac.get(i));
            final String pacName = packageInfo.packageName;

            appName[i] = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
            Log.e("Name", "^^^^^^^^^^^^^^^ " + appName[i]);
            try {
                ApplicationInfo appInfo = getPackageManager().getApplicationInfo(packageInfo.packageName, 0);
                if (packageManager.getLaunchIntentForPackage(appInfo.packageName) != null) {
                    Drawable appicon = null;

                    try {
                        appicon = getPackageManager().getApplicationIcon(pacName);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    arrayOfImages[i] = new ImageView(this);
                    arrayOfImages[i].setImageDrawable(appicon);
                    arrayOfImages[i].setId(i);

                    //Circular imageview for app icon
                    Bitmap bitmap = ((BitmapDrawable) arrayOfImages[i].getDrawable()).getBitmap();
                    RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(DrawAppIcon.this.getResources(), bitmap);
                    roundedBitmapDrawable.setCircular(true);
                    arrayOfImages[i].setImageDrawable(roundedBitmapDrawable);


                    arrayOfImages[i].setX(x + (float) Math.sin(2 * pi * (i - min) / (max - min + 1) + delta_angle) * (float) r);
                    arrayOfImages[i].setY(y - (float) Math.cos(2 * pi * (i - min) / (max - min + 1) + delta_angle) * (float) r);

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(baseIcon_Width, baseIcon_Height);
                    arrayOfImages[i].setLayoutParams(params);
                    // arrayOfImages[i].setVisibility(View.GONE);

                    nn.addView(arrayOfImages[i]);

                    final int finalI = i;
                    arrayOfImages[i].setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                arrayOfImages[finalI].requestLayout();
                                arrayOfImages[finalI].getLayoutParams().width = baseIcon_Width + dTouchSel_Wid;
                                arrayOfImages[finalI].getLayoutParams().height = baseIcon_Height + dtouchSel_Hei;
                            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                                clsFlag = true;
                                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(pacName);
                                startActivity(LaunchIntent);
                                HUD.mButton.setVisibility(View.VISIBLE);

                            }
                            return true;
                        }
                    });
                    //draw inner circle
                    innerCircle.setX(x - Math.round(r / Math.sqrt(2)) + 95);
                    innerCircle.setY(y - Math.round(r / Math.sqrt(2)) + 95);

                    android.view.ViewGroup.LayoutParams layoutParams = innerCircle.getLayoutParams();
                    layoutParams.width = (int) Math.round(r * Math.sqrt(2)) - 120;
                    layoutParams.height = (int) Math.round(r * Math.sqrt(2)) - 120;
                    innerCircle.setLayoutParams(layoutParams);

                    if (i == min) {
                        setContentView(new DrawAlpha(DrawAppIcon.this, x, y, (x + (float) Math.sin(2 * pi * (i - min) / (max - min + 1) + delta_angle) * (float) r), (y - (float) Math.cos(2 * pi * (i - min) / (max- min+ 1) + delta_angle) * (float) r)));
                        Log.e("Letter", " "+ appName[i].toUpperCase().charAt(0));
                        continue;
                    }

                    if (appName[i].toUpperCase().charAt(0)  == appName[i-1].toUpperCase().charAt(0))
                        continue;

                     Log.e("Letter", " "+ appName[i].toUpperCase().charAt(0));
                        setContentView(new DrawAlpha(DrawAppIcon.this, x, y, (x + (float) Math.sin(2 * pi * (i - min) / (max - min + 1) + delta_angle) * (float) r), (y - (float) Math.cos(2 * pi * (i - min) / (max- min+ 1) + delta_angle) * (float) r)));

                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }


        }
//        indexOfFirstApp.add(min);
//        for (int j = min + 1; j <= max - 1; j++) {
//            Log.e("Name", "########### " + appName[j].toUpperCase().charAt(0));
////            if (appName[j - 1].toUpperCase().charAt(0) != appName[j].toUpperCase().charAt(0)) {
//////                indexOfFirstApp.add(j);
////                drawAlpha = new DrawAlpha(DrawAppIcon.this, x + (float) Math.sin(2 * pi * (i - min) / (max - min + 1) + delta_angle) * (float) r, y - (float) Math.cos(2 * pi * (i - min) / (max - min + 1) + delta_angle) * (float) r);
////            }
//        }

    }
}