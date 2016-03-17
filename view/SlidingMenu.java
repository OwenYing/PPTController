package com.example.yangz.pptwithcontrol.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yangz.pptwithcontrol.R;
import com.nineoldandroids.view.ViewHelper;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by yangzhishui on 2015/12/10.
 */
public class SlidingMenu extends HorizontalScrollView {

    private LinearLayout mWapper;
    private ViewGroup mMenu;
    private ViewGroup mContent;

    private boolean once = false;
    private boolean isOpen = true ;
    private int mScreenWidth;
    private int mMenuWidth;
    private int mMenuRightPadding = 5;

    private DatagramSocket sendSocket = null;
    public boolean flag = false;
    private String data = null;





//    ------------------

    /**
     * δʹ���Զ�������ʱ����
     * @param context
     * @param attrs
     */
    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(outMetrics);
//
//        mScreenWidth = outMetrics.widthPixels;
//        //change dp to px
//        mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
    }

    /**
     * ��ʹ�����Զ���������ʱ������ô˹��췽��
     * @param context
     * @param attrs
     * @param defStyle
     */
    public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


        //��ȡ���Ƕ��������
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SlidingMenu,defStyle,0);
        int n = a.getIndexCount();
        for(int i = 0;i < n;i++){
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.SlidingMenu_rightPadding:
                    mMenuRightPadding = a.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP, 5, context
                                            .getResources().getDisplayMetrics()));
                    break;
            }
        }

        a.recycle();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        mScreenWidth = outMetrics.widthPixels;
        //change dp to px
//        mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());

    }
    public SlidingMenu(Context context){
        this(context,null);
    }

    /**
     * ������view�Ŀ�͸�
     * �����Լ��Ŀ�͸�
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!once)
        {
            mWapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWapper.getChildAt(0);
            mContent = (ViewGroup) mWapper.getChildAt(1);
            mMenuWidth =  mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
            mContent.getLayoutParams().width = mScreenWidth ;
            once = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * ͨ������ƫ��������menu����
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if(changed)
        {
            this.scrollTo(mMenuWidth,0);
        }
    }

    //-----------------------------------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case ACTION_UP:
                //��������ߵĿ��
                int scrollX = getScrollX();

                if(scrollX >= mMenuWidth / 2)
                {
                    this.smoothScrollTo(mMenuWidth,0);
                    isOpen = false;
                }
                else {
                    this.smoothScrollTo(0,0);
                    isOpen = true;
                }


                return true;
        }
        return super.onTouchEvent(event);
    }
    /**
     * open menu
     */
    public void openMenu()
    {
        if(isOpen)
            return;
        isOpen = true;
        this.smoothScrollTo(0,0);
    }

    /**
     * close menu
     */
    public void closeMenu()
    {
        if(!isOpen) return;
        isOpen = false;
        this.smoothScrollTo(mMenuWidth, 0);
    }

    public void toggle()
    {
        if(isOpen)
            closeMenu();
        else openMenu();
    }

    /**
     *��������ʱ
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        float scale = l*1.0f/mMenuWidth; //1~0
        //�������Զ���������TranslationX
//        ViewHelper.setTranslationX(mMenu, mMenuWidth * scale);

        /**
         * ����1����������1.0~0.7 ���ŵ�Ч�� scale : 1.0~0.0 0.7 + 0.3 * scale
         *
         * ����2���˵���ƫ������Ҫ�޸�
         *
         * ����3���˵�����ʾʱ�������Լ�͸���ȱ仯 ���ţ�0.7 ~1.0 1.0 - scale * 0.3 ͸���� 0.6 ~ 1.0
         * 0.6+ 0.4 * (1- scale) ;
         *
         */
        float rightScale = 0.7f + 0.3f * scale;
        float leftScale = 1.0f - scale * 0.3f;
        float leftAlpha = 0.6f + 0.4f * (1 - scale);

        // �������Զ���������TranslationX
        ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.8f);

        ViewHelper.setScaleX(mMenu, leftScale);
        ViewHelper.setScaleY(mMenu, leftScale);
        ViewHelper.setAlpha(mMenu, leftAlpha);
        // ����content�����ŵ����ĵ�
        ViewHelper.setPivotX(mContent, 0);
        ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
        ViewHelper.setScaleX(mContent, rightScale);
        ViewHelper.setScaleY(mContent, rightScale);

    }

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



}
