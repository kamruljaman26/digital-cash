package com.example.arafat.dcash.profile;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.arafat.dcash.DCASHMainActivity;
import com.example.arafat.dcash.DigitalCash;
import com.example.arafat.dcash.R;
import com.example.arafat.dcash.api_config.APIConstants;
import com.example.arafat.dcash.extras.BaseActivity;
import com.example.arafat.dcash.extras.LogMe;
import com.example.arafat.dcash.shared_pref.UserPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.arafat.dcash.api_config.APIConstants.ACCTOKENSTARTER;

public class ProfileActivity extends BaseActivity  implements View.OnClickListener {


    private static final String TAG = "ProfileActivity";
    Button btnProfileUpdate, btnLogOut;
    EditText etUserName, etUserMobile, etUserEmail, etUserPass, etUSerDateOfBirth;
    UserPref userPref;

    ImageView ivFB, ivGoogle, ivTwiter, ivLinkdIn;
    String fbLink,twiterLink,gPlusLingk,linkdInLink;
    View parentLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initializeView();


        if(isNetworkAvailable()) {
            getProfileInfo();
        }else {

            Snackbar.make(parentLayout, "Please Check your internet connection!!", Snackbar.LENGTH_LONG)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                    .show();
        }
    }


    private void initializeView() {

        userPref = new UserPref(this);
        parentLayout = findViewById(android.R.id.content);

        btnProfileUpdate = findViewById(R.id.btnProfileUpdate);
        btnLogOut = findViewById(R.id.btnLogOut);

        etUserName = findViewById(R.id.etUserName);
        etUserMobile = findViewById(R.id.etMobileNumber);
        etUserEmail = findViewById(R.id.etUserEmail);
        etUserPass = findViewById(R.id.etPass);
        etUSerDateOfBirth = findViewById(R.id.etDateOfBirth);

        ivFB = findViewById(R.id.ivFaceBook);
        ivGoogle = findViewById(R.id.ivGPlus);
        ivTwiter = findViewById(R.id.ivTwiter);
        ivLinkdIn = findViewById(R.id.ivLinkdIn);

    }


    @Override
    public void onClick(View v) {

        if(v == ivFB){

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fbLink));
            startActivity(browserIntent);
        }
        else if(v == ivGoogle){


            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(gPlusLingk));
            startActivity(browserIntent);
        }
        else if(v == ivTwiter){

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twiterLink));
            startActivity(browserIntent);

        }
        else if(v == ivLinkdIn){

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkdInLink));
            startActivity(browserIntent);
        }
        else if(v == btnProfileUpdate){

            UpdatingProfile();
        }
        else if(v == btnLogOut){


            showProgressDialog();
            loggingOut();

        }

    }

    private void UpdatingProfile() {

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

                            String data = jsonObject.getString("data");
                            LogMe.d("ProfileRes::",data);
                            JSONObject jData = new JSONObject(data);
                            String user = jData.getString("user");
                            LogMe.d("ProfileRes::",user);
                            JSONObject jUser = new JSONObject(user);

                            String UserName= jUser.getString("name");
                            LogMe.d("ProfileRes::",UserName);

                            if(!TextUtils.isEmpty(UserName)){
                                etUserName.setText(UserName);
                            }

                            String Mobile = jUser.getString("phone");
                            if(!TextUtils.isEmpty(Mobile)) {
                                etUserMobile.setText(Mobile);
                            }

                            String Email = jUser.getString("email");
                            if(!TextUtils.isEmpty(Email)) {
                                etUserEmail.setText(Email);
                            }

                            String DateOfBirth = jUser.getString("birthdate");
                            if(!TextUtils.isEmpty(DateOfBirth)) {
                                etUSerDateOfBirth.setText(DateOfBirth);
                            }

                             fbLink = jUser.getString("facebook");
                             gPlusLingk = jUser.getString("google_plus");
                             twiterLink = jUser.getString("twitter");
                             linkdInLink = jUser.getString("linked_in");
                            LogMe.d("ProfileRes::",fbLink);

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
}
