package com.xyz.dcl.bangladesh.earn;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.view.MenuItem;
import android.view.View;


import com.xyz.dcl.bangladesh.R;
import com.xyz.dcl.bangladesh.extras.BaseActivity;

public class IncomeFactory extends BaseActivity {

    private static final String TAG = IncomeFactory.class.getSimpleName();

    private Toolbar toolbar_income_factory;

    private CardView ivVideoWall,ivLookEarn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_factory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initializeView();


        ivVideoWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in =new Intent(IncomeFactory.this, VideoWallActivity.class);
                startActivity(in);
            }
        });


        ivLookEarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in =new Intent(IncomeFactory.this, LookEarnActivity.class);
                startActivity(in);

            }
        });


    }

    private void initializeView(){

        toolbar_income_factory = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar_income_factory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivVideoWall = findViewById(R.id.ivVideoWall);
        ivLookEarn = findViewById(R.id.ivLookandEarn);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
