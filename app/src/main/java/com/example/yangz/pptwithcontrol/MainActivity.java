package com.example.yangz.pptwithcontrol;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yangz.pptwithcontrol.view.SlidingMenu;
import com.example.yangz.pptwithcontrol.view.TouchView;

import org.w3c.dom.Text;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class MainActivity extends Activity implements SensorEventListener {
//------------------------------------------------------------
    private DatagramSocket sendSocket = null;
    private boolean flag = false;
    private String data = null;
    private SensorManager mSensorManager = null;
    float aX , aY , aZ;
    //  float[] gravity = new float[4];

//    TextView tv;
//    ------------------


    private SlidingMenu mLeftMenu;
    private TouchView mTouch;

    //---------------
//    ScrollView fatherView = (ScrollView) findViewById(R.id.id_menu);
//    ScrollView sonView = (ScrollView) findViewById(R.id.touchpad);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mLeftMenu = (SlidingMenu)findViewById(R.id.id_menu);


        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);

//        tv = (TextView)findViewById(R.id.textView);

        //SlidingMenu�е����ݴ���
        mLeftMenu.connect();
        connect();

        final ImageButton playnowbt = (ImageButton) findViewById(R.id.playnowbt);
        final ImageButton previouspage = (ImageButton) findViewById(R.id.backpage);
        final ImageButton playbutton = (ImageButton) findViewById(R.id.playbutton);
        final ImageButton next = (ImageButton) findViewById(R.id.next);

        final ImageButton pen = (ImageButton) findViewById(R.id.pen);
        final ImageButton laser = (ImageButton) findViewById(R.id.laser);
        final ImageButton touchpadbt = (ImageButton) findViewById(R.id.touchpadbt);
        final ImageButton control = (ImageButton) findViewById(R.id.control);
        final ImageButton control2 = (ImageButton) findViewById(R.id.control2);

        final ImageButton helpbt = (ImageButton) findViewById(R.id.help);
        final ImageButton endbt = (ImageButton) findViewById(R.id.end);

        endbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = "9";
                flag = true;
            }
        });

        helpbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHelpMenu = new Intent();
                toHelpMenu.setClass(MainActivity.this,HelpMenu.class);
                MainActivity.this.startActivity(toHelpMenu);
            }
        });

        playnowbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // data = "1";
                // flag = true;
            }
        });

       control.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {

               data = "a," + aX + "," + aY + "," + aZ;
               flag = true;

               return false;
           }
       });

        control2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = "b," + aX + "," + aY + "," + aZ;
                flag = true;
            }
        });

        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = "3";
                flag = true;
            }
        });
        playnowbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = "4";
                flag = true;
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = "5";
                flag = true;
            }
        });

        previouspage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = "6";
                flag = true;
            }
        });
        pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = "7";
                flag = true;
            }
        });
        laser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                data = "8";
                flag = true;
            }
        });

        touchpadbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toTouchpad = new Intent();
                toTouchpad.setClass(MainActivity.this,TouchView.class);
                MainActivity.this.startActivity(toTouchpad);
            }
        });


    }

    public void toggleMenu(View view){
        mLeftMenu.toggle();
    }



//    //-----------------------------------------------------
//    /*
//        Main func of the net Control
//     */
//    public void connect() {
//
//
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//                    sendSocket = new DatagramSocket();
//
//                    while(true)
//                        if (flag) {
//                            byte[] buf = data.getBytes();
//                            DatagramPacket dp = new DatagramPacket(buf, buf.length, InetAddress.getByName("192.168.43.255"), 11111);
//                            sendSocket.send(dp);
//                            flag = false;
//                        }
//                } catch(Exception e)
//                {
//                    Log.e("Eeeeeeeee", e.getClass().toString());
//                    e.printStackTrace();
//                    Log.e("Eeeeeeeee" , "eeeeeeeee");
//                }
//
//            }
//        });
//
//        thread.start();
//
//    }
////-------------------------------------------------


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


    //----------------------------------------------------
    /*
    * Sensor
    *
    * */
    @Override
    public void onSensorChanged(SensorEvent event) {

        switch(event.sensor.getType()) {

            case Sensor.TYPE_ACCELEROMETER:

//                final float alpha = (float) 0.8;
//                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
//                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
//                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
//
//                aX = event.values[0]-gravity[0];
//                aY = event.values[1]-gravity[1];
//                aZ = event.values[2]-gravity[2];
//                aX = event.values[0];
//                aY = event.values[1];
//                aZ = event.values[2];
//
//                tv.setText("ax:"+aX+",ay:"+aY+",az:"+aZ);

                //  data = "a,"+aX+","+aY+","+aZ;
                //  flag = true;

                break;
            case Sensor.TYPE_GRAVITY:
//                gravity[0] = event.values[0];
//                gravity[1] = event.values[1];
//                gravity[2] = event.values[2];
                break;

            case Sensor.TYPE_GYROSCOPE:


                break;

            case Sensor.TYPE_ORIENTATION:
                aX = event.values[0];
                aY = event.values[1];
                aZ = event.values[2];

//                tv.setText("ax:"+aX+",ay:"+aY+",az:"+aZ);

                break;

            default:
                break;

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }




    //--------------------------------------------------
    /*
       Use Volume to Control the slides
     */
    private long mExitTime;
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

            case KeyEvent.KEYCODE_BACK:
                mLeftMenu.toggle();
                if ((System.currentTimeMillis() - mExitTime) > 300){
                    Object mHelperUtils;
//                    Toast.makeText(this,"�ٰ�һ���˳�����",Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                }
                else{
                    String msg = "����˫���˳�����";
                    Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
                    finish();
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
//----------------------------------------------------



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
