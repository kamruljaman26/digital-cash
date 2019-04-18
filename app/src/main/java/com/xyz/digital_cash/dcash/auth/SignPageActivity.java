package com.xyz.digital_cash.dcash.auth;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xyz.digital_cash.dcash.DCASHMainActivity;
import com.xyz.digital_cash.dcash.DigitalCash;
import com.xyz.digital_cash.dcash.R;
import com.xyz.digital_cash.dcash.api_config.APIConstants;
import com.xyz.digital_cash.dcash.extras.BaseActivity;
import com.xyz.digital_cash.dcash.extras.LogMe;
import com.xyz.digital_cash.dcash.privacy_policy.PrivacyPolicyActivity;
import com.xyz.digital_cash.dcash.shared_pref.UserPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignPageActivity extends BaseActivity implements View.OnClickListener {


    private static final String TAG = SignPageActivity.class.getSimpleName();
    LinearLayout llSignUp,llSignIn;
    Button btnSignInSelection,btnSignUpSelection;
    CardView cvSignUpSelection;

    private UserPref userPref;

    //SignUp Attributes
    EditText etRUserName,etRUserEmail,etRUserPassword,etRUserMobile,etRUserDOB,etRUserBkash,etRUserReferral,etRUserCity;
    CardView cvSignUpButton;
    Calendar myCalender;
    DatePickerDialog.OnDateSetListener date;


    //SignIn Attributes
    EditText etUserEmail,etUserPassword;
    CardView cvSignInButton;
    private String Name,Pass,City,DOB,Phone,Email,Referral,Bkash;
    View parentLayout;

    TextView tvPPIn, tvPPUp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeView();
        chekPreviousLogedIn();

    }

    private void initializeView() {

        userPref = new UserPref(this);
        myCalender = Calendar.getInstance();
        parentLayout = findViewById(android.R.id.content);

        llSignIn = findViewById(R.id.llSignIn);
        llSignUp = findViewById(R.id.llSignUp);


        btnSignInSelection = findViewById(R.id.btnSigninSelection);
        btnSignInSelection.setOnClickListener(this);
        btnSignUpSelection = findViewById(R.id.btnSignupSelection);
        btnSignUpSelection.setOnClickListener(this);
        cvSignUpSelection = findViewById(R.id.cvSignUpSelection);
        cvSignUpSelection.setOnClickListener(this);

        //SignIn Attributes
        etUserEmail = findViewById(R.id.etUserEmail);
        etUserPassword = findViewById(R.id.etUserPassword);
        cvSignInButton = findViewById(R.id.cvSignInButton);
        cvSignInButton.setOnClickListener(this);
        tvPPIn = findViewById(R.id.tvInPrivacyPolicy);
        tvPPIn.setOnClickListener(this);

        //SignUp Attributes
        etRUserName = findViewById(R.id.etRUserName);
        etRUserEmail = findViewById(R.id.etRUserEmail);
        etRUserPassword = findViewById(R.id.etRUserPassword);
        etRUserMobile = findViewById(R.id.etRUserMobile);
        etRUserDOB = findViewById(R.id.etRUserDateOfBirth);
        etRUserDOB.setOnClickListener(this);
        etRUserBkash = findViewById(R.id.etRUserBkash);
        etRUserReferral = findViewById(R.id.etRUserReferral);
        etRUserCity = findViewById(R.id.etRUserCity);

        cvSignUpButton =findViewById(R.id.cvSignUpButton);
        cvSignUpButton.setOnClickListener(this);
        tvPPUp = findViewById(R.id.tvUpPrivacyPolicy);
        tvPPUp.setOnClickListener(this);

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                LogMe.d("mo::", month + "--" + year + "--" + dayOfMonth);

                myCalender.set(Calendar.YEAR, year);
                myCalender.set(Calendar.MONTH, month);
                myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateView(year, month + 1, dayOfMonth);
            }
        };
    }

    @Override
    public void onClick(View v) {

        if (v == btnSignInSelection){

            llSignIn.setVisibility(View.VISIBLE);
            llSignUp.setVisibility(View.GONE);
            btnSignInSelection.setTextColor(getResources().getColor(R.color.white));
            btnSignUpSelection.setTextColor(getResources().getColor(R.color.ash));

            chekPreviousLogedIn();
        }
        else if (v == btnSignUpSelection){

            llSignIn.setVisibility(View.GONE);
            llSignUp.setVisibility(View.VISIBLE);
            btnSignInSelection.setTextColor(getResources().getColor(R.color.ash));
            btnSignUpSelection.setTextColor(getResources().getColor(R.color.white));
        }
        else if(v == cvSignUpSelection){
            llSignIn.setVisibility(View.GONE);
            llSignUp.setVisibility(View.VISIBLE);
            btnSignInSelection.setTextColor(getResources().getColor(R.color.ash));
            btnSignUpSelection.setTextColor(getResources().getColor(R.color.white));
        }
        else if(v == cvSignInButton){

            getSigningIn();
        }
        else if(v == cvSignUpButton){

            getSiningUp();
        }
        else if(v == etRUserDOB){
            new DatePickerDialog(SignPageActivity.this, date, myCalender
                    .get(Calendar.YEAR), myCalender.get(Calendar.MONTH),
                    myCalender.get(Calendar.DAY_OF_MONTH)).show();

        }else if(v == tvPPIn){

            Intent in = new Intent(SignPageActivity.this, PrivacyPolicyActivity.class);
            startActivity(in);
        }else if(v == tvPPUp){

            Intent in = new Intent(SignPageActivity.this, PrivacyPolicyActivity.class);
            startActivity(in);
        }
    }

    private void updateView(int y, int m, int d) {

        String month = "", day = "";


        if (m < 10) {
            month = "0" + m;
        } else {
            month = "" + m;
        }

        if (d < 10) {
            day = "0" + d;
        } else {
            day = "" + d;
        }
        etRUserDOB.setText(y + "-" + month + "-" + day);

    }

    private void getSiningUp() {

        Name = etRUserName.getText().toString();
        Email = etRUserEmail.getText().toString();
        Pass = etRUserPassword.getText().toString();
        Phone = etRUserMobile.getText().toString();
        DOB = etRUserDOB.getText().toString();
        Bkash = etRUserBkash.getText().toString();
        Referral = etRUserReferral.getText().toString();
        City =etRUserCity.getText().toString();

        if(TextUtils.isEmpty(Name) || TextUtils.isEmpty(Email) || TextUtils.isEmpty(Pass) || TextUtils.isEmpty(Phone) || TextUtils.isEmpty(DOB) || TextUtils.isEmpty(City) || TextUtils.isEmpty(Bkash) || TextUtils.isEmpty(Referral)){

            if(TextUtils.isEmpty(Name)){

                etRUserName.setError("Please enter your name!");

            }else if(TextUtils.isEmpty(Email)){

                etRUserEmail.setError("Please enter your Email!");

            }else if(TextUtils.isEmpty(Pass)){

                etRUserPassword.setError("Please enter your Password!");

            }else if(TextUtils.isEmpty(Phone)){

                etRUserMobile.setError("Please enter your Mobile!");

            }else if(TextUtils.isEmpty(DOB)){

                etRUserDOB.setError("Please enter your Birth date!");

            }else if(TextUtils.isEmpty(Bkash)){

                etRUserBkash.setError("Please enter bkash trans Id!");

            }else if(TextUtils.isEmpty(Referral)){

                etRUserReferral.setError("Please enter your Referral!");

            }else if(TextUtils.isEmpty(City)){

                etRUserCity.setError("Please enter your City!");

            }
        }else {

            showProgressDialog();

            getRegister();

        }

    }

    private void getRegister() {



        StringRequest request = new StringRequest(Request.Method.POST,APIConstants.Auth.REGISTER, createMyReqSuccessListener(),createMyReqErrorListener()){

            @Override
            public byte[] getBody() throws com.android.volley.AuthFailureError {
                String str = "{\"name\":\""+Name+"\",\"email\":\""+Email+"\",\"password\":\""+Pass+"\",\"phone\":\""+Phone+"\",\"birthdate\":\""+DOB+"\",\"city\":\""+City+"\",\"transaction_id\":\""+Bkash+"\",\"referral_id\":\""+Referral+"\"}";

                String adb = "{\"name\": \""+Name+"\",\"email\": \""+Email+"\",\"password\": \""+Pass+"\",\"phone\": \""+Phone+"\",\"birthdate\":\""+DOB+"\",\"city\": \""+City+"\",\"transaction_id\": \""+Bkash+"\",\"referral_id\": \""+Referral+"\"}";
                LogMe.d("data::",adb);
                return adb.getBytes();
            };

            public String getBodyContentType()
            {
                return "application/json; charset=utf-8";
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(request);
    }

    private Response.Listener<String> createMyReqSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogMe.i(TAG,"Ski data from server - "+response);
                hideProgressDialog();

                try {
                    JSONObject jObj = new JSONObject(response);


                    if(jObj.has("success")){

                        if(jObj.getString("success").equals("true")){

                            JSONObject obj = new JSONObject(jObj.getString("data"));
                            userPref.setUserEmail(Email);
                            userPref.setUserPasssword(Pass);

                            Intent in =new Intent(SignPageActivity.this, DCASHMainActivity.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(in);
                            finish();

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
    }


    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogMe.i(TAG,"Ski error connect - "+error);
                hideProgressDialog();
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data

                        LogMe.d("er::",res);
                        JSONObject obj = new JSONObject(res);

                        String errMsg  = obj.getString("message");
                        //View parentLayout = findViewById(android.R.id.content);
                       /* Snackbar.make(parentLayout, errMsg, Snackbar.LENGTH_LONG)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                .show();*/

                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(SignPageActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(SignPageActivity.this);
                        }
                        builder.setTitle("ALERT!!!")
                                .setMessage(errMsg)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
            }
        };
    }



    //SIGNIN METHOD

    private void chekPreviousLogedIn() {

        if(TextUtils.isEmpty(userPref.getUserEmail())){

            LogMe.d(TAG,"No info about login");
        }else {

            etUserEmail.setText(userPref.getUserEmail());
            etUserPassword.setText(userPref.getUserPasssword());
        }
    }

    private void getSigningIn() {



        String mail = etUserEmail.getText().toString().trim();
        String pass = etUserPassword.getText().toString();

        LogMe.d("sign::",mail+"--"+pass);

        if(TextUtils.isEmpty(mail) || TextUtils.isEmpty(pass)){

            if(TextUtils.isEmpty(mail)){

                etUserEmail.setError(" Please give your email!");
            }else if(TextUtils.isEmpty(pass)){

                etUserPassword.setError(" Please give your password!");
            }

        }else {

            if(isNetworkAvailable()) {
                showProgressDialog();
                DCASHSignIn(mail, pass);
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

    }

    private void DCASHSignIn(final String mail, final String pass) {

        LogMe.d("LoginRes::","LOGIN");

        StringRequest request = new StringRequest(Request.Method.POST, APIConstants.Auth.LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                LogMe.d("LoginRes::",response);

                hideProgressDialog();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String ApiSuccess = jsonObject.getString("success");

                    if (ApiSuccess == "true"){

                        JSONObject jObj = new JSONObject(jsonObject.getString("data"));
                        String accToken = jObj.getString("access_token");
                        LogMe.d("accTok::",accToken);

                        userPref.setUserAccessToken(accToken);
                        userPref.setUserEmail(mail);
                        userPref.setUserPasssword(pass);


                        Intent in =new Intent(SignPageActivity.this,DCASHMainActivity.class);
                        startActivity(in);
                        finish();


                    }else {

                        Toast.makeText(getApplicationContext(), "Error Occured: "+jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                hideProgressDialog();

                NetworkResponse response = error.networkResponse;

//                LogMe.d("er::",response.toString());
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data

                        LogMe.d("er::",res);
                        int stCode = response.statusCode;
                        View parentLayout = findViewById(android.R.id.content);

                        if(stCode == 500){

                            Snackbar.make(parentLayout, "Server Error! Please try again later", Snackbar.LENGTH_LONG)
                                    .setAction("CLOSE", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                    .show();
                        }else {
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", mail);
                params.put("password",pass);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
        DigitalCash.getDigitalCash().addToRequestQueue(request, TAG);
    }

}
