package com.xyz.dcl.bangladesh.earn;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.InterstitialAd;
import com.xyz.dcl.bangladesh.R;

public class FBWallActivity extends AppCompatActivity {


    private static final String TAG = FBWallActivity.class.getSimpleName();
    private InterstitialAd interstitialAd;

    Button btnLoadAgain;
    Toolbar toolbar_fb_wall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbwall);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar_fb_wall = (Toolbar) findViewById(R.id.toolbar_fb_wall);
        setSupportActionBar(toolbar_fb_wall);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btnLoadAgain = findViewById(R.id.btnfbLoadAgain);
        btnLoadAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFBAd();

            }
        });



    }

    private void loadFBAd() {

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
    }


}
