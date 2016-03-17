package com.example.yangz.pptwithcontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by yangz on 2015/12/23.
 */
public class Login extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        ImageButton enter = (ImageButton) findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMain = new Intent().setClass(Login.this,MainActivity.class);
                startActivity(toMain);
            }
        });


    }
}
