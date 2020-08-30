package com.xyz.digital_cash.dcash.cash_out.tab_activities;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xyz.digital_cash.dcash.R;

public class TabCashOutHistory extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_cash_out_history,container,false);

        return rootView;
    }
}
