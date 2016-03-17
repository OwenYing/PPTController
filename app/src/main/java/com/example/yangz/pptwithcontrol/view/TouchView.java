package com.example.yangz.pptwithcontrol.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yangz.pptwithcontrol.MainActivity;
import com.example.yangz.pptwithcontrol.R;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by yangzhishui on 2015/12/16.
 */
public class TouchView extends Activity {

    private DatagramSocket sendSocket = null;
    public boolean flag = false;
    private String data = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touchpad);

        connect();

        final ImageButton gohome = (ImageButton) findViewById(R.id.gohome);
        final ImageButton leftclick = (ImageButton) findViewById(R.id.leftclick);
        final ImageButton rightclick = (ImageButton) findViewById(R.id.rightclick);
        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        leftclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = "1";
                flag = true;
            }
        });

//        leftclick.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                int action = motionEvent.getAction();
//
//                switch (action) {
//                    case MotionEvent.ACTION_DOWN:
//                        data = "p";
//                        flag = true;
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        data = "r";
//                        flag = true;
//                        break;
//                }
//
//
//                return false;
//            }
//        });

        rightclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = "2";
                flag = true;
            }
        });

    }



    private VelocityTracker vt = null;
    double clickTime=0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        if(action == MotionEvent.ACTION_DOWN)
            clickTime = System.currentTimeMillis();


        switch (action)
        {
            case MotionEvent.ACTION_DOWN:

                if(vt == null){
                    vt = VelocityTracker.obtain();
                }
                else {
                    vt.clear();
                }
                vt.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                vt.addMovement(event);
                //������Ǽ��ÿ10������ָ�ƶ��ľ��루���أ���m/px��������������vt�ĵ�λ����������Ϊ1�������1������ָ�ƶ�������������ms/px
                vt.computeCurrentVelocity(10);
                //����xΪ���������ָ���һ�����Ϊ���������ָ���󻬶���y�ıȽ����⣬Ϊ���������ָ���»�����Ϊ���������ָ���ϻ���

                data = "c,"+vt.getXVelocity()+","+vt.getYVelocity();
                flag = true;
                break;
            case ACTION_UP:

                double isClick = System.currentTimeMillis()-clickTime;
                // tv.setText("last:"+clickTime+" , now:"+System.currentTimeMillis()+"\n"+isClick);
                if(isClick < 100)
                {
                    data = "1";
                    flag = true;
                }
                return true;
        }
        return super.onTouchEvent(event);

    }

    //--------------------------------------------------
    /*
       Use Volume to Control the slides
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                data = "5";
                flag = true;

                return true;

            case KeyEvent.KEYCODE_VOLUME_UP:
                data = "6";
                flag = true;

                return true;


        }
        return super.onKeyDown(keyCode, event);
    }
    //----------------------------------------------------
    //-----------------------------------------------------
    /*
        Main func of the net Control
     */
    public void connect() {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    sendSocket = new DatagramSocket();

                    while(true)
                        if (flag) {
                            byte[] buf = data.getBytes();
                            DatagramPacket dp = new DatagramPacket(buf, buf.length, InetAddress.getByName("192.168.43.255"), 11111);
                            sendSocket.send(dp);
                            flag = false;
                        }
                } catch(Exception e)
                {
                    Log.e("Eeeeeeeee", e.getClass().toString());
                    e.printStackTrace();
                    Log.e("Eeeeeeeee" , "eeeeeeeee");
                }

            }
        });

        thread.start();

    }
//-------------------------------------------------



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
