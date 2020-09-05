package com.xyz.dcl.bangladesh.earn;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.noqoush.adfalcon.android.sdk.ADFAd;
import com.noqoush.adfalcon.android.sdk.ADFInterstitial;
import com.noqoush.adfalcon.android.sdk.ADFListener;
import com.noqoush.adfalcon.android.sdk.constant.ADFErrorCode;
import com.xyz.dcl.bangladesh.R;
import com.xyz.dcl.bangladesh.extras.BaseActivity;

public class FalconAdActivity extends BaseActivity implements ADFListener {

    private static final String FALCON_APP_ID = "44efffaab650467cadf1981f550eaae7";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falcon_ad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_falcon_ad);
        setSupportActionBar(toolbar);


        ADFInterstitial adfInterstitial = new ADFInterstitial(this, FALCON_APP_ID, this);
        adfInterstitial.loadInterstitialAd();

    }

    @Override
    public void onLoadAd(ADFAd ad) {
        if(ad instanceof ADFInterstitial){
            ((ADFInterstitial)ad).showInterstitialAd();
        }
    }

    @Override
    public void onError(ADFAd ad, ADFErrorCode code, String message) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(FalconAdActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(FalconAdActivity.this);
        }
        builder.setTitle("ALERT!!!")
                .setMessage(code+": "+message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        /*if(ad instanceof ADFInterstitial){
            if(code == ADFErrorCode.NO_AVAILABLE_AD){
                //Do anything here

                LogMe.d("eCode:",code+" NO Ad");
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(FalconAdActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(FalconAdActivity.this);
                }
                builder.setTitle("ALERT!!!")
                        .setMessage(code+": "+message)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }else if(code == ADFErrorCode.COMMUNICATION_ERROR){
                //Do anything here

                LogMe.d("eCode:",code+" Comm Err");

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(FalconAdActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(FalconAdActivity.this);
                }
                builder.setTitle("ALERT!!!")
                        .setMessage(code+": "+message)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }else if(code == ADFErrorCode.MISSING_PARAM){
                //Do anything here

                LogMe.d("eCode:",code+" Missing Param");

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(FalconAdActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(FalconAdActivity.this);
                }
                builder.setTitle("ALERT!!!")
                        .setMessage(code+": "+message)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }else{
                //Do anything here
            }
        }*/
    }

    @Override
    public void onPresentAdScreen(ADFAd adfAd) {

    }

    @Override
    public void onDismissAdScreen(ADFAd adfAd) {

    }

    @Override
    public void onLeaveApplication() {

    }
}
