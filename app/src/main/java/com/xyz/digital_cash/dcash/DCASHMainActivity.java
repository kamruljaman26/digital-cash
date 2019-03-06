package com.xyz.digital_cash.dcash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.xyz.digital_cash.dcash.api_config.APIConstants;
import com.xyz.digital_cash.dcash.banner.BannerSlider;
import com.xyz.digital_cash.dcash.banner.BannerSliderAdapter;
import com.xyz.digital_cash.dcash.banner.BannerSliderModel;
import com.xyz.digital_cash.dcash.cash_out.CashOutActivity;
import com.xyz.digital_cash.dcash.earn.IncomeFactory;
import com.xyz.digital_cash.dcash.extras.BaseActivity;
import com.xyz.digital_cash.dcash.extras.LogMe;
import com.xyz.digital_cash.dcash.profile.UserProfileActivity;
import com.xyz.digital_cash.dcash.shared_pref.UserPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.http.GET;

import static com.xyz.digital_cash.dcash.api_config.APIConstants.ACCTOKENSTARTER;
import static com.xyz.digital_cash.dcash.api_config.APIConstants.General.BANNERIMAGESLIDE;

public class DCASHMainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Toolbar toolbar;
    private static final String TAG = "DCASHMainActivity";
    CardView cardEarnByIvite;
    UserPref userPref;
    CardView cardIncomeFactory,cardShopEarn,cardMyShop,cardMyNetwork,cardEarningPlan,cardExclusiveOffers,cardEarnMore;
    NavigationView navigationView;
    TextView tvUserNmae, tvuserEmail,tvUserReferral, tvUserAvailableBalance,tvUserEarningBalance;


    //Slider for homePage
    private static ViewPager mPagerImageSlider;
    private static int currentBannerSlider = 0;
    ArrayList<BannerSlider> bannerSliderArrayList = new ArrayList<BannerSlider>();
    private LinearLayout llBannerDots;
    private int dotscount;
    private ImageView[] dots;
    private Timer swipeTimer;
    private Handler handler;
    private Runnable Update;
    private ProgressBar progressBarHomePageBannerSlider;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dcashmain);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*initializeView();*/
        getDrawerNavigation();
        initializeView();
        getProfileInfo();
        //getImageBannerSlider();
        getImageBanner();
        getImageBannerSlider();


        //getBannerSliderFromServer();


    }

    private void getDrawerNavigation(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initializeView() {

        progressBarHomePageBannerSlider = findViewById(R.id.progressBarHomePageBannerSlider);

        mPagerImageSlider =  findViewById(R.id.pager_image_slider_home_page);
        llBannerDots = findViewById(R.id.llBannerDots);


        cardIncomeFactory = findViewById(R.id.cvIncomeFactory);
        cardIncomeFactory.setOnClickListener(this);

        userPref = new UserPref(DCASHMainActivity.this);
        View headerView = navigationView.getHeaderView(0);

        tvUserNmae = headerView.findViewById(R.id.tvUserName);
        tvuserEmail = headerView.findViewById(R.id.tvUserEmail);
        tvUserReferral = headerView.findViewById(R.id.tvUserReferral);
        tvUserAvailableBalance = headerView.findViewById(R.id.tvUserCurrentBalance);
        tvUserEarningBalance = headerView.findViewById(R.id.tvUserEarningBalance);

    }

    private void getImageBanner(){

        BannerSlider bannerSlider = new BannerSlider();

        for (int i=0;i<5;i++){

            bannerSlider.setBannerID("1");

            if(i ==0)
            bannerSlider.setIm(getResources().getDrawable(R.drawable.test1));
            if(i ==1)
                bannerSlider.setIm(getResources().getDrawable(R.drawable.test2));
            if(i ==2)
                bannerSlider.setIm(getResources().getDrawable(R.drawable.test3));
            if(i ==3)
                bannerSlider.setIm(getResources().getDrawable(R.drawable.test4));
            if(i ==4)
                bannerSlider.setIm(getResources().getDrawable(R.drawable.test5));

            bannerSlider.setGivenData("given_data");
            bannerSlider.setContentID("content_id");

            bannerSliderArrayList.add(bannerSlider);
        }
    }

    private void getImageBannerSlider() {
        StringRequest request = new StringRequest(Request.Method.GET, BANNERIMAGESLIDE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressBarHomePageBannerSlider.setVisibility(View.GONE);
                LogMe.e(TAG, " data Image Slider:: " + response);
                JSONObject jObj = null;

                try {

                    jObj = new JSONObject(response);
                    if (jObj.getString("success").equals("true")) {

                        String apiData = jObj.getString("data");
                        JSONObject jObj2 = new JSONObject(apiData);
                        final JSONArray bannerArray = jObj2.getJSONArray("banner_images");
                        try {
                            for (int i = 0; i < bannerArray.length(); i++) {
                                JSONObject json = null;

                                json = bannerArray.getJSONObject(i);
                                BannerSlider bannerSlider = new BannerSlider();
                                bannerSlider.setBannerID(json.getString("banner_type_id"));
                                bannerSlider.setFileName(json.getString("file_name"));
                                bannerSlider.setGivenData(json.getString("given_data"));
                                bannerSlider.setContentID(json.getString("content_id"));

                                //bannerSliderArrayList.add(bannerSlider);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        mPagerImageSlider.setAdapter(new BannerSliderAdapter(DCASHMainActivity.this, bannerSliderArrayList));

                        getBannerSliderDots();


                        handler = new Handler();
                        Update = new Runnable() {

                            public void run() {
                               /* if (currentBannerSlider == bannerSliderArrayList.size()) {
                                    currentBannerSlider = 0;
                                }
                                mPagerImageSlider.setCurrentItem(currentBannerSlider++, true);
                                handler.postDelayed(this,5000);*/

                                int pos = currentBannerSlider++;
                                LogMe.d("current::", pos + "--" + bannerArray.length());
                                if (pos == bannerArray.length()) {
                                    currentBannerSlider = 0;
                                }

                                mPagerImageSlider.setCurrentItem(pos);

                            }
                        };

                        swipeTimer = new Timer();
                        swipeTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {

                                handler.post(Update);


                            }
                        }, 2000, 2000);




                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogMe.d(TAG + " err:: ", error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "9qM55rm9lirQEbdNfqxUah3oNpboOcUP1yADLFz2Kc78mKPft7CemLbpZI49ASvsBWcACgjsVh2DEv0YutYlgjwezjF5dRM6XBxWtKIkDkzFh1HG3alJu4Ce2ydUBH8Z");

                return params;
            }

        };

        DigitalCash.getDigitalCash().addToRequestQueue(request, TAG);
    }

    private void getBannerSliderDots() {
        dotscount = bannerSliderArrayList.size();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            llBannerDots.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        mPagerImageSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void getProfileInfo() {

            StringRequest request = new StringRequest(Request.Method.POST, APIConstants.Auth.USER_PROFILE, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    LogMe.d("ProfileRes::",response);

                    hideProgressDialog();

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if(jsonObject.has("success")) {
                            String ApiSuccess = jsonObject.getString("success");

                            if (ApiSuccess == "true") {


                                JSONObject jObj =new JSONObject(jsonObject.getString("data"));

                                String Name = jObj.getString("name");
                                String Email = jObj.getString("email");
                                String Referral = jObj.getString("referral_link");
                                String AvailableBalance = jObj.getString("available_balance");
                                String EarningBalance = jObj.getString("earning_balance");

                                tvUserNmae.setText(Name);
                                tvuserEmail.setText(Email);
                                tvUserReferral.setText("R/F : "+Referral);
                                tvUserAvailableBalance.setText(AvailableBalance +"BDT");
                                tvUserEarningBalance.setText(EarningBalance+ " BDT");
                                userPref.setUserEarningBalance(EarningBalance);

                            }
                        }else {

                            Toast.makeText(getApplicationContext(), "Error Occured: " + jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogMe.d("ProfileRes::",e.toString());
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    hideProgressDialog();
                    LogMe.d(TAG,"er::"+ APIConstants.Auth.USER_PROFILE);

                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            // Now you can use any deserializer to make sense of data

                            int stCode = response.statusCode;
                            View parentLayout = findViewById(android.R.id.content);

                            if (stCode == 500) {

                                Snackbar.make(parentLayout, "Server Error! Please try again later", Snackbar.LENGTH_LONG)
                                        .setAction("CLOSE", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                            }
                                        })
                                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                        .show();
                            } else {
                                LogMe.d("er::", res);
                                JSONObject obj = new JSONObject(res);

                                String errMsg = obj.getString("message");

                                Snackbar.make(parentLayout, errMsg, Snackbar.LENGTH_LONG)
                                        .setAction("CLOSE", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                            }
                                        })
                                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                        .show();
                            }

                        } catch (UnsupportedEncodingException e1) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (JSONException e2) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put("Authorization", ACCTOKENSTARTER+userPref.getUserAccessToken());

                    LogMe.d(TAG,userPref.getUserAccessToken());

                    return params;
                }
            };
            DigitalCash.getDigitalCash().addToRequestQueue(request, TAG);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dcashmain, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       if(id == R.id.nav_logout){

           showProgressDialog();
           loggingOut();
       }else if(id == R.id.nav_profile){

           Intent in =new Intent(this,UserProfileActivity.class);
           startActivity(in);
       }else if(id == R.id.nav_cashout){
           Intent in =new Intent(this,CashOutActivity.class);
           startActivity(in);
       }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

//        tvUserEarningBalance.setText(userPref.getUserEarningBalance()+ " BDT");
    }

    @Override
    public void onClick(View v) {

        /*if( v == cardEarnByIvite){
            Intent in =new Intent(DCASHMainActivity.this,EarnByInvitingActivity.class);
            startActivity(in);
        }
        else if( v == cardIncomeFactory){
            Intent in =new Intent(DCASHMainActivity.this,IncomeFactory.class);
            startActivity(in);
        }else if( v == cardShopEarn){
           underConstruction();
        }else if( v == cardMyShop){
           underConstruction();
        }else if( v == cardMyNetwork){
            underConstruction();
        }else if( v == cardEarningPlan){
            underConstruction();
        }else if( v == cardExclusiveOffers){
            underConstruction();
        }else if( v == cardEarnMore){
            underConstruction();
        }*/

        if( v == cardIncomeFactory){
            Intent in =new Intent(DCASHMainActivity.this,IncomeFactory.class);
            startActivity(in);
        }

    }

    /*private void getBannerSliderFromServer() {

        StringRequest request = new StringRequest(Request.Method.GET, APIConstants.General.SLIDER_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                LogMe.d("slideS::",response);

                JSONObject object;

                try {

                    object = new JSONObject(response);

                    if(object.has("success") && object.getString("success").contains("true")){

                        JSONArray dataArray = object.getJSONArray("data");
                        LogMe.d("slideS::",dataArray.toString());

                        for (int i = 0;i<dataArray.length();i++){



                        }

                    }else {


                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                error.printStackTrace();
                hideProgressDialog();
                LogMe.d(TAG,"er::"+ APIConstants.Auth.USER_PROFILE);

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data

                        int stCode = response.statusCode;
                        View parentLayout = findViewById(android.R.id.content);

                        if (stCode == 500) {

                            Snackbar.make(parentLayout, "Server Error! Please try again later", Snackbar.LENGTH_LONG)
                                    .setAction("CLOSE", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                    .show();
                        } else {
                            LogMe.d("er::", res);
                            JSONObject obj = new JSONObject(res);

                            String errMsg = obj.getString("message");

                            Snackbar.make(parentLayout, errMsg, Snackbar.LENGTH_LONG)
                                    .setAction("CLOSE", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                    .show();
                        }

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization", ACCTOKENSTARTER+userPref.getUserAccessToken());

                LogMe.d(TAG,userPref.getUserAccessToken());

                return params;
            }
        };
        DigitalCash.getDigitalCash().addToRequestQueue(request, TAG);

    }*/

}
