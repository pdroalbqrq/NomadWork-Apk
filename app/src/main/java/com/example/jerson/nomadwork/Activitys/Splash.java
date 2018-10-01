package com.example.jerson.nomadwork.Activitys;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jerson.nomadwork.BasicClass.Local;
import com.example.jerson.nomadwork.R;
import com.example.jerson.nomadwork.Util.Utils;

public class Splash extends AppCompatActivity implements Runnable {
    private final int DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash );
        Handler handler = new Handler();
        handler.postDelayed( this, DELAY );
    }

    @Override
    public void run() {
        Intent intent = new Intent( getApplicationContext(), LoginActivity.class );
        startActivity( intent );

        finish();
    }
}
