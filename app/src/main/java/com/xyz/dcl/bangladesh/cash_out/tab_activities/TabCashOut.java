package com.xyz.dcl.bangladesh.cash_out.tab_activities;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.xyz.dcl.bangladesh.DigitalCash;
import com.xyz.dcl.bangladesh.R;
import com.xyz.dcl.bangladesh.api_config.APIConstants;
import com.xyz.dcl.bangladesh.extras.LogMe;
import com.xyz.dcl.bangladesh.shared_pref.UserPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.xyz.dcl.bangladesh.api_config.APIConstants.ACCTOKENSTARTER;

public class TabCashOut extends Fragment implements View.OnClickListener {


    CardView cvBkash, cvManual, cvTcash, cvMcash, cvSurecash, cvRoket;
    String payAmount = "";
    String payMethod = "Manual";
    String payNumber = "";


    TextView earningBalance, currentBalance;

    UserPref userPref;
    String TAG = TabCashOut.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cash_out,container,false);

        initalizaView(rootView);

        return rootView;
    }

    private void initalizaView(View rootView) {

        userPref = new UserPref(getActivity());

        cvBkash =rootView.findViewById(R.id.cvBkash);
        cvBkash.setOnClickListener(this);
        cvManual =rootView.findViewById(R.id.cvManual);
        cvManual.setOnClickListener(this);
        cvTcash =rootView.findViewById(R.id.cvTkash);
        cvTcash.setOnClickListener(this);
        cvMcash =rootView.findViewById(R.id.cvMcash);
        cvMcash.setOnClickListener(this);
        cvSurecash =rootView.findViewById(R.id.cvSeurCash);
        cvSurecash.setOnClickListener(this);
        cvRoket =rootView.findViewById(R.id.cvRoket);
        cvRoket.setOnClickListener(this);

        //init Earning & Current Balance
        earningBalance = rootView.findViewById(R.id.cout_earning_balance_id);
        currentBalance = rootView.findViewById(R.id.cout_current_balance_id);

        //set Earning & Current Balance
        setBalance();

    }

    @Override
    public void onClick(View v) {

        if(v== cvBkash){
            CashOutDialog(0);

        }else if(v== cvManual){

            CashOutDialog(1);
        }else if(v== cvMcash){

            CashOutDialog(0);
        }else if(v== cvRoket){
            CashOutDialog(0);

        }else if(v== cvSurecash){
            CashOutDialog(0);

        }else if(v== cvTcash){

            CashOutDialog(0);
        }
    }

    private void CashOutDialog(final int i) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_cash_out, null);
        alertDialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        final EditText etPayAmount = dialogView.findViewById(R.id.etPaymentAmount);
        final EditText etPayMethod = dialogView.findViewById(R.id.etPaymentMethod);
        final EditText etPayNumber = dialogView.findViewById(R.id.etPaymentNumber);
        Button btnPayRequest = dialogView.findViewById(R.id.btnCashOutRequest);

        if(i == 1){

            etPayNumber.setVisibility(View.GONE);
            etPayMethod.setVisibility(View.GONE);

        }

        btnPayRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    payAmount = etPayAmount.getText().toString();
                    payMethod = etPayMethod.getText().toString();
                    payNumber = etPayNumber.getText().toString();

                    if(i == 0){

                        if(TextUtils.isEmpty(payAmount) || TextUtils.isEmpty(payMethod) || TextUtils.isEmpty(payNumber)){

                            Toast.makeText(getActivity().getApplicationContext(),"Please give all the data",Toast.LENGTH_LONG).show();

                        }else {

                            alertDialog.cancel();
                            CashOutRequest(payAmount,payMethod,payNumber);
                        }

                    }else {
                        if(TextUtils.isEmpty(payAmount)){

                            Toast.makeText(getActivity().getApplicationContext(),"Please give all the data",Toast.LENGTH_LONG).show();
                        }else {

                            alertDialog.cancel();
                            CashOutRequest(payAmount,"cash",payNumber);
                        }

                    }
            }
        });

    }

    private void CashOutRequest(final String payAmount, final String cash, final String payNumber) {

        StringRequest request = new StringRequest(Request.Method.POST, APIConstants.General.CASHOUT_REQUEST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                LogMe.d("cashout::",response);



                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has("success")) {
                        String ApiSuccess = jsonObject.getString("success");

                        if (ApiSuccess == "true") {

                           /* new AlertDialog.Builder(getActivity())
                                    .setTitle("CASHOUT")
                                    .setMessage(" CAsh Out reqest submitted successfully")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.cancel();
                                        }
                                    })
                                    .setIcon(R.mipmap.ic_launcher_round)
                                    .show();*/

                        } else {

                           }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data

                        LogMe.d("erres::",res);

                        int stCode = response.statusCode;
                        View parentLayout = getActivity().findViewById(android.R.id.content);

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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params =new HashMap<>();

                params.put("amount", payAmount);
                params.put("payment_method",cash);
                params.put("payment_number",payNumber);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String,String> headers = new HashMap<>();

                /*headers.put("Content-Type", "application/json");*/
                headers.put("Accept", "application/json");
                headers.put("Authorization", ACCTOKENSTARTER+userPref.getUserAccessToken());

                return headers;
            }
        };
        DigitalCash.getDigitalCash().addToRequestQueue(request, TAG);

        /*JSONObject params = new JSONObject();
        try {
            params.put("amount", payAmount);
            params.put("payment_method", payMethod);
            params.put("payment_number", payNumber);

        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                APIConstants.General.CASHOUT_REQUEST, params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //pDialog.hide();
            }
        });

        DigitalCash.getDigitalCash().addToRequestQueue(jsonObjReq, TAG);*/

    }

    private void setBalance(){
        StringRequest request = new StringRequest(Request.Method.POST, APIConstants.Auth.USER_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                LogMe.d("ProfileRes::",response);

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

                            currentBalance.setText(AvailableBalance +" BDT");
                            earningBalance.setText(EarningBalance+ " BDT");
                            Log.d(TAG, "onResponse: "+AvailableBalance+" "+EarningBalance);
                            userPref.setUserEarningBalance(EarningBalance);

                        }
                    }else {
                        Toast.makeText(getActivity(), "Error Occured: " + jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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
