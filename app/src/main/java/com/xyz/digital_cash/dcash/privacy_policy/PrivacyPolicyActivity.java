package com.xyz.digital_cash.dcash.privacy_policy;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xyz.digital_cash.dcash.R;
import com.xyz.digital_cash.dcash.api_config.APIConstants;
import com.xyz.digital_cash.dcash.extras.BaseActivity;

public class PrivacyPolicyActivity extends BaseActivity {

    WebView wvPrivacyPolicy;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        wvPrivacyPolicy=findViewById(R.id.wvPrivacyPolicy);
        wvPrivacyPolicy.setWebViewClient(new MyBrowser());


        wvPrivacyPolicy.getSettings().setLoadsImagesAutomatically(true);
        wvPrivacyPolicy.getSettings().setJavaScriptEnabled(true);
        wvPrivacyPolicy.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wvPrivacyPolicy.loadUrl(APIConstants.PRIVACY_POLICY);

    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            showProgressDialog();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            hideProgressDialog();

            super.onPageFinished(view, url);
        }
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
