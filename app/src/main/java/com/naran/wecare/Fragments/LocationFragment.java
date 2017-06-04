package com.naran.wecare.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.naran.wecare.R;


public class LocationFragment extends WeCareFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_location,container,false);
        initiliseView(view);
        initialiseListener();

        return view;
    }

    @Override
    protected void initiliseView(View view) {

    }

    @Override
    protected void initialiseListener() {

    }
}
