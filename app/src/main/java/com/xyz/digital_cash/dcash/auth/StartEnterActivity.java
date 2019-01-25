package com.xyz.digital_cash.dcash.auth;

import android.content.Intent;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.xyz.digital_cash.dcash.R;

public class StartEnterActivity extends AppCompatActivity {

    private String DEBUG_TAG = StartEnterActivity.class.getSimpleName();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_enter);

        View myView =findViewById(R.id.myView);
        myView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = MotionEventCompat.getActionMasked(motionEvent);

                switch(action) {

                    case (MotionEvent.ACTION_UP) :
                        Log.d(DEBUG_TAG,"Action was UP");

                        Intent in =new Intent(StartEnterActivity.this,SignPageActivity.class);
                        startActivity(in);
                        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                        finish();
                        return true;

                    default :
                        return true;
                }
            }
        });
    }
}
