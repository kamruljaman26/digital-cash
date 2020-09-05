package com.xyz.dcl.bangladesh.cash_out.tab_activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xyz.dcl.bangladesh.DigitalCash;
import com.xyz.dcl.bangladesh.R;
import com.xyz.dcl.bangladesh.api_config.APIConstants;
import com.xyz.dcl.bangladesh.extras.LogMe;
import com.xyz.dcl.bangladesh.shared_pref.UserPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.xyz.dcl.bangladesh.api_config.APIConstants.ACCTOKENSTARTER;

public class TabCashOutHistory extends Fragment {

    private TextView earningBalance, currentBalance;
    UserPref userPref;
    String TAG = getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_cash_out_history,container,false);

        userPref = new UserPref(getActivity());
        initalizaView(rootView);

        return rootView;
    }

    private void initalizaView(View rootView) {

        //init Earning & Current Balance
        earningBalance = rootView.findViewById(R.id.cout_h_earning_balance_id);
        currentBalance = rootView.findViewById(R.id.cout_h_current_balance_id);

        //set Earning & Current Balance
        setBalance();
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
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", ACCTOKENSTARTER+userPref.getUserAccessToken());

                return params;
            }
        };
        DigitalCash.getDigitalCash().addToRequestQueue(request, TAG);
    }
}
