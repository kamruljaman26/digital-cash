package com.xyz.dcl.bangladesh;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.xyz.dcl.bangladesh.auth.StartEnterActivity;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 3s = 3000ms
                Intent in =new Intent(LoadingActivity.this,StartEnterActivity.class);
                startActivity(in);
                finish();
            }
        }, 4000);
    }
}
