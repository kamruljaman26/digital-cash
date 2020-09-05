package com.xyz.dcl.bangladesh;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

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
import com.xyz.dcl.bangladesh.api_config.APIConstants;
import com.xyz.dcl.bangladesh.banner.BannerSlider;
import com.xyz.dcl.bangladesh.banner.BannerSliderAdapter;
import com.xyz.dcl.bangladesh.cash_out.CashOutActivity;
import com.xyz.dcl.bangladesh.earn.IncomeFactory;
import com.xyz.dcl.bangladesh.extras.BaseActivity;
import com.xyz.dcl.bangladesh.extras.LogMe;
import com.xyz.dcl.bangladesh.privacy_policy.PrivacyPolicyActivity;
import com.xyz.dcl.bangladesh.profile.UserProfileActivity;
import com.xyz.dcl.bangladesh.shared_pref.UserPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.xyz.dcl.bangladesh.api_config.APIConstants.ACCTOKENSTARTER;

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
    //ArrayList<BannerSlider> bannerSliderArrayList = new ArrayList<BannerSlider>();
    ArrayList<String> bannerSliderImages = new ArrayList<>();
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
        //getImageBanner();
        //getImageBannerSlider();
        //getBannerSliderFromServer();

        getSliderBannerIm();

    }

    @Override
    protected void onStart() {
        navigationView.setCheckedItem(R.id.nav_home);
        super.onStart();
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

    private void getSliderBannerIm(){

        StringRequest request = new StringRequest(Request.Method.GET, APIConstants.General.SLIDER_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                /*bannerSliderArrayList = new ArrayList<BannerSlider>();
                bannerSliderArrayList.clear();*/
                progressBarHomePageBannerSlider.setVisibility(View.GONE);
                bannerSliderImages = new ArrayList<>();
                //bannerSliderImages.clear();
                LogMe.d("slideS::",response);

                JSONObject object;

                try {

                    object = new JSONObject(response);

                    if(object.has("success") && object.getString("success").contains("true")){

                        JSONArray dataArray = object.getJSONArray("data");
                        LogMe.d("slideS::",dataArray.toString());
                        BannerSlider bannerSlider = new BannerSlider();

                        for (int i = 0;i<dataArray.length();i++){


                            JSONObject jObj = dataArray.getJSONObject(i);
                            Log.d("images::",jObj.getString("image"));

                            bannerSliderImages.add(jObj.getString("image"));

                           /* bannerSlider.setFileName(jObj.getString("image"));
                            Log.d("images::",bannerSlider.getFileName());
                            bannerSliderArrayList.add(i,bannerSlider);
                            Log.d("images::",bannerSliderArrayList.get(i).getFileName());*/
                        }


                        for (int i = 0;i<bannerSliderImages.size();i++){

                            Log.d("b:",bannerSliderImages.get(i)+"---"+i);

                        }

                        mPagerImageSlider.setAdapter(new BannerSliderAdapter(DCASHMainActivity.this, bannerSliderImages));

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

                                if (pos == bannerSliderImages.size()) {
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
                        }, 3000, 3000);

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
    }


    private void getBannerSliderDots() {
        dotscount = bannerSliderImages.size();
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.dcashmain, menu);
//        return true;
//    }


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
       }else if(id == R.id.nav_privacy_policy){
           Intent in =new Intent(this, PrivacyPolicyActivity.class);
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

        if( v == cardIncomeFactory){
            Intent in =new Intent(DCASHMainActivity.this,IncomeFactory.class);
            startActivity(in);
        }

    }

}
